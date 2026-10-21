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
	 * Subtotal amount of the invoice, before applying VAT. This represents the taxable base.
	 */
	private Money subtotal = Money.ZERO;  // cents

	/*
	 * VAT rate applied to the invoice, expressed as a percentage. Example: 21% VAT.
	 */
	private Percentage vatRate = Percentage.ofPercent(21);  // default to 21%

	/*
	 * VAT amount calculated from the subtotal amount. This is the tax the customer must pay.
	 */
	private Money vatAmount = Money.ZERO;  // cents

	/*
	 * Total amount of the invoice including VAT. This is the final amount the customer must pay.
	 */
	private Money total = Money.ZERO; // cents

	// Accidental attributes
	private Set<WorkOrder> workOrders = new HashSet<>();
	private Set<Charge> charges = new HashSet<>();

	// ----------------- Constructors -------------------------------------
	
	public Invoice(long number) {
		// call full constructor with sensible defaults
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public Invoice(long number, LocalDate date) {
		// call full constructor with sensible defaults
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public Invoice(long number, List<WorkOrder> workOrders) {
		this(number, LocalDate.now(), workOrders);
	}

	public Invoice(long number, LocalDate date, List<WorkOrder> workOrders) {
		// check arguments (always), through IllegalArgumentException
		// store the number
		// add every work order calling addWorkOrder( w )
		throw new UnsupportedOperationException("Not implemented yet");
	}

	// ---- Getters ----
	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workOrders);
	}

	public Set<Charge> getCharges() {
		return new HashSet<>(charges);
	}

	// ------------------ Association helpers (package-private) --------------------------
	
	Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}

	Set<Charge> _getCharges() {
		return charges;
	}

	// ---- Business methods --------------------------------------------
	
	/**
	 * Adds a work order to the invoice. The work order must be approved and the invoice must not be settled.
	 * @param workOrder
	 */
	public void addWorkOrder(WorkOrder workOrder) {
		// Validations
		// links the work order 
		// compute the invoice amount after adding the work order
		// marks it as invoiced
		
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Removes a work order from the invoice. The invoice must not be settled.
	 * @param workOrder
	 */
	public void removeWorkOrder(WorkOrder workOrder) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public boolean isSettled() {
		return state == InvoiceState.PAID;
	}

	/**
	 * Settles the invoice by charging the specified payment means with the requested amounts.
	 * The total of the charges must equal the invoice total, and each payment mean must be able to be charged.
	 * @param requestedCharges A map of payment means and their corresponding amounts to charge.
	 */
	public void settle(Map<PaymentMean, Money> requestedCharges) {
		// Validations
		// Normalize the requested charges (round to cents, check positive)
		// Check that the total of requested charges equals the invoice total
		// For each payment mean, create a Charge and link it to the invoice and payment mean
		// Set the invoice state to PAID

		throw new UnsupportedOperationException("Not implemented yet");
	}

	public void unSettle() {
		StateChecks.isTrue(isSettled(), "Invoice is not settled");

		new HashSet<>(charges).forEach(Charge::rewind);
		state = InvoiceState.ISSUED;
	}

	// ---- Calculation ----
	private void computeAmount() {
	    // 1. Sum all work order totals = subtotal
	    // 2. Determine VAT rate based on invoice date
	    // 3. Compute VAT amount
	    // 4. Compute total amount (subtotal + VAT) rounded to cents
		throw new UnsupportedOperationException("Not implemented yet");
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

}
