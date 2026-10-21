package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;

public class Mechanic {

	// Natural attributes
	private Nif nif;
	private FullName fullName;

	// Accidental attributes
	private Set<WorkOrder> assigned = new HashSet<>();
	
	public Mechanic(Nif nif) {
		// Delegate to the other constructor with meaningful default values for the fullname
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public Mechanic(Nif nif, FullName name) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	// ---- Setters (optional, controlled) ----

	public void setFullName(FullName name) {
		ArgumentChecks.isNotNull(name);
		this.fullName = name;
	}
	
	public void setNif(Nif nif) {
		ArgumentChecks.isNotNull(nif);
		this.nif = nif;		
	}

	// ---- Associations helpers ----

	Set<WorkOrder> _getAssigned() {
		return assigned;
	}

}
