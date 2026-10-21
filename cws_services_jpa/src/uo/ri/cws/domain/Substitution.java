package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

/**
 * A substitution is a relation between an intervention and a spare part. 
 * It represents the fact that a spare part has been used in an intervention.
 * 
 * Internal aggregate class of WorkOrder. Only Intervention can create Substitutions (through WorkOrder).
 */
public class Substitution {
	// Natural attributes
    private Money price;
	private int quantity;

	// Accidental attributes
	private SparePart sparePart;
	private Intervention intervention;
    

	// ---------------------------- Constructors -----------------------------
	/**
	 * Package-private constructor to create a new Substitution with the given parameters.
	 * Only Intervention can create Substitutions.
	 */
	/* package */ Substitution(SparePart sparePart, Intervention intervention, int quantity) {
		// Validate input parameters
		// assignations
		// link the substitution with the intervention and spare part (atomiclink)
		throw new UnsupportedOperationException("Not implemented yet");
	}

	// ---------------------------- Association helpers -----------------------------
	
	void _setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
	}

	void _setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}

	// ---------------------------- Business methods -----------------------------
	public Money getTotalAmount() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
