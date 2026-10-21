package uo.ri.cws.domain;

import uo.ri.cws.domain.types.Money;

public class Cash extends PaymentMean {

	// ---- Constructors ----
	
	public Cash(Client client) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	// ---- Business methods ----
	@Override
	public boolean canBeCharged(Money amount) {
		checkPositive(amount);
		return true;
	}

	// ---- toString ----
	@Override
	public String toString() {
		return "Cash [accumulated=" + getAccumulated() + ", client=" + getClient() + "]";
	}

}
