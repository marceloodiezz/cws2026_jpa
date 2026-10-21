package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

public class CreditCard extends PaymentMean {

	private CreditCardNumber number;
    private String type;
    private LocalDate validThru;

    // ---- Constructors ----
    public CreditCard(CreditCardNumber number, String type, YearMonth validThru) {
        ArgumentChecks.isNotNull(number, "card number cannot be null");
        ArgumentChecks.isNotBlank(type, "card type cannot be null or blank");
        ArgumentChecks.isNotNull(validThru, "validThru cannot be null");

        this.number = number;
        this.type = type;
        this.validThru = validThru.atEndOfMonth(); // internal conversion for mapping simplicity
    }

    // ---- Business methods ----
    public boolean isValidNow() {
    	return ! validThru.isBefore( LocalDate.now() ); // validThru >= today, today included
    }

	/**
	 * A credit card can pay if is not outdated and the amount is positive. 
	 */
    @Override
    public boolean canBeCharged(Money amount) {
    	throw new UnsupportedOperationException("Not implemented yet");
    }
}
