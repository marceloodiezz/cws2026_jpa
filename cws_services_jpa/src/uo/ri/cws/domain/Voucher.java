package uo.ri.cws.domain;

import java.util.Objects;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
public class Voucher extends PaymentMean {

	@AttributeOverride(name = "code", column = @Column(unique = true))
	private VoucherCode code;
	private String description;
	@AttributeOverride(name = "amount", column = @Column(name = "AVAILABLEAMOUNT", precision = 10, scale = 2))
	private Money availableAmount; // cents

	// ---- Constructors ----
	Voucher() {
	}

	public Voucher(VoucherCode code, Money available) {
		this(code, "no-description", available);
	}

	public Voucher(VoucherCode code, String description, Money available) {
		ArgumentChecks.isNotNull(code);
		ArgumentChecks.isNotBlank(description);
		ArgumentChecks.isNotNull(available);
		ArgumentChecks.isTrue(available.isNonNegative(), "Available amount must be non-negative");

		this.code = code;
		this.description = description;
		this.availableAmount = Money.calc(available).toMoneyRoundedToCents();
	}

	// ------ Getters and setters (if needed) ----

	public Money getAvailableAmount() {
		return availableAmount;
	}

	public String getDescription() {
		return description;
	}

	public void changeDescription(String description) {
		ArgumentChecks.isNotBlank(description);
		this.description = description;
	}

	public VoucherCode getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "Voucher [code=" + code + ", description=" + description + ", availableAmount=" + availableAmount + "]";
	}

	// ---- Business methods ----

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Voucher other = (Voucher) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	/* package */ void charge(Money amount) {
		// Charges the amount to the payment mean and subtracts it from the available
		// amount
		Money normalizedAmount = normalizePositive(amount);

		super.charge(normalizedAmount);

		availableAmount = Money.calc(availableAmount).subtract(normalizedAmount).toMoneyRoundedToCents();
	}

	@Override
	/* package */ void rewind(Money amount) {
		// Rewinds the amount to the payment mean and adds it to the available amount
		Money normalizedAmount = normalizePositive(amount);

		super.rewind(normalizedAmount);

		availableAmount = Money.calc(availableAmount).add(normalizedAmount).toMoneyRoundedToCents();
	}

	@Override
	public boolean canBeCharged(Money amount) {
		checkPositive(amount);
		return availableAmount.greaterOrEqual(amount);
	}

}
