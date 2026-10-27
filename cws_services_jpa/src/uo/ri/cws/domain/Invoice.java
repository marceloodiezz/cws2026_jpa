package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Percentage;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class Invoice {
	private static final LocalDate JULY_1_2012 = LocalDate.of(2012, 7, 1);

	public enum InvoiceState {
		ISSUED, PAID
	}

	// Natural attributes
	private long number;
	private LocalDate date;
	private InvoiceState state = InvoiceState.ISSUED;

	/*
	 * Subtotal amount of the invoice, before applying VAT. This represents the
	 * taxable base.
	 */
	private Money subtotal = Money.ZERO; // cents

	/*
	 * VAT rate applied to the invoice, expressed as a percentage. Example: 21% VAT.
	 */
	private Percentage vatRate = Percentage.ofPercent(21); // default to 21%

	/*
	 * VAT amount calculated from the subtotal amount. This is the tax the customer
	 * must pay.
	 */
	private Money vatAmount = Money.ZERO; // cents

	/*
	 * Total amount of the invoice including VAT. This is the final amount the
	 * customer must pay.
	 */
	private Money total = Money.ZERO; // cents

	// Accidental attributes
	private Set<WorkOrder> workOrders = new HashSet<>();
	private Set<Charge> charges = new HashSet<>();

	// ----------------- Constructors -------------------------------------

	public Invoice(long number) {
		this(number, LocalDate.now(), List.of());
	}

	public Invoice(long number, LocalDate date) {
		this(number, date, List.of());
	}

	public Invoice(long number, List<WorkOrder> workOrders) {
		this(number, LocalDate.now(), workOrders);
	}

	public Invoice(long number, LocalDate date, List<WorkOrder> workOrders) {
		// check arguments (always), through IllegalArgumentException
		// store the number
		// add every work order calling addWorkOrder( w )
		ArgumentChecks.isTrue(number >= 0, "Invoice number must be positive");
		ArgumentChecks.isNotNull(date, "Invoice date cannot be null");
		ArgumentChecks.isNotNull(workOrders, "Work orders list cannot be null");

		for (WorkOrder workOrder : workOrders) {
			ArgumentChecks.isNotNull(workOrder, "Work order cannot be null");

			StateChecks.isTrue(workOrder.isApproved(), "Work order is not approved");

			StateChecks.isTrue(workOrder.getInvoice() == null, "Work order already has an invoice");
		}

		this.number = number;
		this.date = date;

		workOrders.forEach(this::addWorkOrder);
	}

	// ---- Getters ----
	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workOrders);
	}

	public Set<Charge> getCharges() {
		return new HashSet<>(charges);
	}

	public Money getSubtotal() {
		return subtotal;
	}

	public Percentage getVatRate() {
		return vatRate;
	}

	public Money getVatAmount() {
		return vatAmount;
	}

	public Money getTotal() {
		return total;
	}

	// ------------------ Association helpers (package-private)
	// --------------------------

	Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}

	Set<Charge> _getCharges() {
		return charges;
	}

	// ---- Business methods --------------------------------------------

	/**
	 * Adds a work order to the invoice. The work order must be approved and the
	 * invoice must not be settled.
	 * 
	 * @param workOrder
	 */
	public void addWorkOrder(WorkOrder workOrder) {
		// Validations
		ArgumentChecks.isNotNull(workOrder, "Work order cannot be null");

		StateChecks.isTrue(!isSettled(), "Invoice is settled");
		StateChecks.isTrue(workOrder.isApproved(), "Work order is not approved");

		// links the work order
		Associations.Bills.link(this, workOrder);

		// compute the invoice amount after adding the work order
		computeAmount();

		// marks it as invoiced
		workOrder.invoice();
	}

	/**
	 * Removes a work order from the invoice. The invoice must not be settled.
	 * 
	 * @param workOrder
	 */
	public void removeWorkOrder(WorkOrder workOrder) {
		ArgumentChecks.isNotNull(workOrder, "Work order cannot be null");

		StateChecks.isTrue(!isSettled(), "Invoice is settled");

		ArgumentChecks.isTrue(workOrders.contains(workOrder), "Work order does not belong to this invoice");

		workOrder.uninvoice();

		Associations.Bills.unlink(workOrder);

		computeAmount();
	}

	public boolean isSettled() {
		return state == InvoiceState.PAID;
	}

	/**
	 * Settles the invoice by charging the specified payment means with the
	 * requested amounts. The total of the charges must equal the invoice total, and
	 * each payment mean must be able to be charged.
	 * 
	 * @param requestedCharges A map of payment means and their corresponding
	 *                         amounts to charge.
	 */
	public void settle(Map<PaymentMean, Money> requestedCharges) {
		// Validations
		// Normalize the requested charges (round to cents, check positive)
		// Check that the total of requested charges equals the invoice total
		// For each payment mean, create a Charge and link it to the invoice and payment
		// mean
		// Set the invoice state to PAID

		StateChecks.isTrue(!isSettled(), "Invoice is already settled");
		ArgumentChecks.isNotNull(requestedCharges, "Requested charges cannot be null");
		ArgumentChecks.isTrue(!requestedCharges.isEmpty(), "Requested charges cannot be empty");

		Map<PaymentMean, Money> normalizedCharges = normalize(requestedCharges);

		StateChecks.isTrue(total.greaterThan(Money.ZERO), "Invoice total must be positive");

		StateChecks.isTrue(chargesTotal(normalizedCharges).equals(total), "Charges total must equal invoice total");

		normalizedCharges.forEach((paymentMean, amount) -> {
			StateChecks.isTrue(paymentMean.canBeCharged(amount), "Payment mean cannot be charged");
		});
		
		normalizedCharges.forEach((paymentMean, amount) -> {
	        new Charge(this, paymentMean, amount);
	    });

		state = InvoiceState.PAID;
	}

	public void unSettle() {
		StateChecks.isTrue(isSettled(), "Invoice is not settled");

		new HashSet<>(charges).forEach(Charge::rewind);
		state = InvoiceState.ISSUED;
	}

	// ---- Calculation ----
	private void computeAmount() {
		// 1. Sum all work order totals = subtotal
		Money.MoneyCalculator calculator = Money.calc();

		for (WorkOrder workOrder : workOrders) {
			calculator.add(workOrder.getAmount());
		}

		this.subtotal = calculator.toMoneyRoundedToCents();

		// 2. Determine VAT rate based on invoice date
		this.vatRate = getApplicableVatRate();

		// 3. Compute VAT amount
		this.vatAmount = Money.calc(subtotal).times(vatRate).toMoneyRoundedToCents();

		// 4. Compute total amount (subtotal + VAT) rounded to cents
		this.total = Money.calc(subtotal).add(vatAmount).toMoneyRoundedToCents();
	}

	private Percentage getApplicableVatRate() {
		return date.isAfter(JULY_1_2012) ? Percentage.ofPercent(21) : Percentage.ofPercent(18);
	}

	private Map<PaymentMean, Money> normalize(Map<PaymentMean, Money> requestedCharges) {
		Map<PaymentMean, Money> result = new LinkedHashMap<>();
		requestedCharges.forEach((paymentMean, amount) -> {
			ArgumentChecks.isNotNull(paymentMean);
			ArgumentChecks.isNotNull(amount);
			Money normalizedAmount = Money.calc(amount).toMoneyRoundedToCents();
			ArgumentChecks.isTrue(normalizedAmount.greaterThan(Money.ZERO), "Charge amount must be positive");
			result.put(paymentMean, normalizedAmount);
		});
		return result;
	}

	private Money chargesTotal(Map<PaymentMean, Money> requestedCharges) {
		Money.MoneyCalculator calculator = Money.calc();
		requestedCharges.values().forEach(calculator::add);
		return calculator.toMoneyRoundedToCents();
	}

	public long getNumber() {
		return number;
	}

	public LocalDate getDate() {
		return date;
	}

	public InvoiceState getState() {
		return state;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Long.valueOf(number));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Invoice other = (Invoice) obj;
		return number == other.number;
	}

	@Override
	public String toString() {
		return "Invoice [number=" + number + ", date=" + date + ", state=" + state + "]";
	}

}
