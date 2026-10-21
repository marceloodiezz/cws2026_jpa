package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.cws.domain.types.Money;

public class VehicleType {

	private String name;
	private Money pricePerHour;

	public VehicleType(String name, Money pricePerHour) {
		super();
		this.name = name;
		this.pricePerHour = pricePerHour;
	}

	public String getName() {
		return name;
	}

	public Money getPricePerHour() {
		return pricePerHour;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, pricePerHour);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VehicleType other = (VehicleType) obj;
		return Objects.equals(name, other.name) && Objects.equals(pricePerHour, other.pricePerHour);
	}

	@Override
	public String toString() {
		return "VehicleType [name=" + name + ", pricePerHour=" + pricePerHour + "]";
	}

}
