package uo.ri.cws.infrastructure.persistence.jpa.query;

import uo.ri.cws.application.query.ClientQueries;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class ClientJpaQueries implements ClientQueries {

	@Override
	public boolean hasPaidInvoices(String clientId) {
		Long count = Jpa.getManager().createNamedQuery("Client.hasPaidInvoices", Long.class).setParameter(1, clientId)
				.getSingleResult();

		return count > 0;
	}

}
