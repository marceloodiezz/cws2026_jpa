package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;

public class ClientJpaRepository 
		extends BaseJpaRepository<Client> 
		implements ClientRepository {

	@Override
    public Optional<Client> findByNif(Nif nif) {
		throw new UnsupportedOperationException("Not implemented yet");
    }


}
