package uo.ri.cws.application.repository;

import java.util.Optional;

import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.types.Nif;

public interface ClientRepository extends Repository<Client> {

	/**
	 * @param nif
	 * @return the Client identified by the nif or null if none
	 */
	Optional<Client> findByNif(Nif nif);
}
