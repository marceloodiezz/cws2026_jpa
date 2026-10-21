package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.util.assertion.ArgumentChecks;

public class Client {

	// Natural attributes
	private Nif nif;
	private FullName fullName;
	private Email email;
	private PhoneNumber phone;
	private Address address;

	// ---- Constructors ----
	public Client(Nif nif) {
		this(nif, 
			new FullName("No name", "No surname"), 
			new Email("default@example.com"),
			new PhoneNumber("+34600000000"), 
			new Address("No street", "No city", "No zip", "No country")
		);
	}

	public Client(Nif nif, FullName fullName) {
	    this(nif,
	        fullName,
	        new Email("default@example.com"),
	        new PhoneNumber("+34600000000"),
			new Address("No street", "No city", "No zip", "No country")
	    );
	}
	
	public Client(Nif nif, FullName fullName, Email email, PhoneNumber phone, Address address) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}