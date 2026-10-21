package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

public class SparePart {
	
	// Natural attributes
	private String code;
	private String description;
	private Money unitPrice;

	public SparePart(String code) {
		this( code, "no-description", Money.ZERO );
	}

	public SparePart(String code, String description, Money price) {
		// Argument checks
		// Round the price to cents
		
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
}

