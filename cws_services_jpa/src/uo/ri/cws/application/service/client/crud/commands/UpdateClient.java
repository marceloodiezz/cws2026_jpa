package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateClient implements Command<Void> {

	private ClientDto dto;

	private ClientRepository clientRepo = Factories.repository.forClient();

	public UpdateClient(ClientDto dto) {
		ArgumentChecks.isNotNull(dto, "The client cannot be null");

		ArgumentChecks.isNotBlank(dto.id, "The client id cannot be blank");
		ArgumentChecks.isNotBlank(dto.nif, "The nif cannot be blank");
		ArgumentChecks.isNotBlank(dto.name, "The name cannot be blank");
		ArgumentChecks.isNotBlank(dto.surname, "The surname cannot be blank");
		ArgumentChecks.isNotBlank(dto.email, "The email cannot be blank");
		ArgumentChecks.isNotBlank(dto.phone, "The phone cannot be blank");

		ArgumentChecks.isNotBlank(dto.addressStreet, "The street cannot be blank");
		ArgumentChecks.isNotBlank(dto.addressCity, "The city cannot be blank");
		ArgumentChecks.isNotBlank(dto.addressCountry, "The country cannot be blank");
		ArgumentChecks.isNotBlank(dto.addressZipcode, "The zipcode cannot be blank");

		this.dto = dto;
	}

	@Override
	public Void execute() throws BusinessException {
		Optional<Client> oc = clientRepo.findById(dto.id);

		BusinessChecks.exists(oc, "The client does not exist");

		Client client = oc.get();

		BusinessChecks.hasVersion(client.getVersion(), dto.version,
				"The client has been updated by another transaction");

		Nif newNif = new Nif(dto.nif);

		if (!client.getNif().equals(newNif)) {
			BusinessChecks.doesNotExist(clientRepo.findByNif(newNif), "There is already a client with the same nif");
		}

		if (!client.getNif().equals(newNif)) {
			client.setNif(newNif);
		}

		client.setFullName(new FullName(dto.name, dto.surname));
		client.setEmail(new Email(dto.email));
		client.setPhone(new PhoneNumber(dto.phone));
		client.setAddress(new Address(dto.addressStreet, dto.addressCity, dto.addressCountry, dto.addressZipcode));

		client.updatedNow();

		return null;
	}
}
