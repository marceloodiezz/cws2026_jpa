package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

public class Vehicle {

	// Natural attributes
	private LicensePlate licensePlate;
	private String make;
	private String model;
	
	// Accidental attributes
	private VehicleType vehicleType;

	// ---- Constructors ----

	public Vehicle(LicensePlate plateNumber) {
		// Delegate to the other constructor with meaningful default values for make and model
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public Vehicle(LicensePlate plateNumber, String make, String model) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public Money getPricePerHour() {
		return vehicleType.getPricePerHour(); // VehicleType must return Money
	}
}
