package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class Charge {

	// Natural attributes
	private Money amount; // cents

	// Accidental attributes
	private Invoice invoice;
	private PaymentMean paymentMean;

	// ------ Constructors -----

	/* package */ Charge(Invoice invoice, PaymentMean paymentMean, Money amount) {
		// validates the arguments
		// store the amount rounded to cents
		// increments the paymentMean accumulated -> paymentMean.charge( amount )
		// links invoice, this and paymentMean
		ArgumentChecks.isNotNull(invoice, "Invoice cannot be null");
		ArgumentChecks.isNotNull(paymentMean, "Payment mean cannot be null");

		Money normalizedAmount = PaymentMean.normalizePositive(amount);

		paymentMean.charge(normalizedAmount);

		this.amount = normalizedAmount;

		Associations.SettlesWithdraws.atomicLink(invoice, this, paymentMean);
	}

	// ---- Getters ----

	public Money getAmount() {
		return amount;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public PaymentMean getPaymentMean() {
		return paymentMean;
	}

	// ---- Bussiness methods ----

	/**
	 * Restores and the value to the payment mean and unlinks the charge from the
	 * invoice and the payment mean.
	 */
	/* package */ void rewind() {
		StateChecks.isTrue(paymentMean != null, "Charge has no payment mean");
		StateChecks.isTrue(invoice != null, "Charge has no invoice");

		paymentMean.rewind(amount);

		Associations.SettlesWithdraws.atomicUnlink(this);
	}

	// ---- Association helpers ----

	void _setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	void _setPaymentMean(PaymentMean paymentMean) {
		this.paymentMean = paymentMean;
	}

	@Override
	public int hashCode() {
		return Objects.hash(invoice, paymentMean);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Charge other = (Charge) obj;
		return Objects.equals(invoice, other.invoice) && Objects.equals(paymentMean, other.paymentMean);
	}

	// ---- toString ----
	@Override
	public String toString() {
		return "Charge [invoice=" + invoice + ", paymentMean=" + paymentMean + ", amount=" + amount + "]";
	}
}
