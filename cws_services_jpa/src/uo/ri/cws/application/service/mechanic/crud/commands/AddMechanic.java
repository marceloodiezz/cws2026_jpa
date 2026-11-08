package uo.ri.cws.application.service.mechanic.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic implements Command<MechanicDto> {

	private MechanicDto dto;
	private MechanicRepository repo = Factories.repository.forMechanic();

	public AddMechanic(MechanicDto dto) {
		ArgumentChecks.isNotNull(dto, "The mechanic dto is null");
		ArgumentChecks.isNotBlank(dto.nif, "The mechanic nif is null");
		ArgumentChecks.isNotBlank(dto.name, "The mechanic name is null");
		ArgumentChecks.isNotBlank(dto.surname, "The mechanic surname is null");
		this.dto = dto;
	}

	@Override
	public MechanicDto execute() throws BusinessException {

		Nif nif = new Nif(dto.nif);
		FullName name = new FullName(dto.name, dto.surname);

		BusinessChecks.doesNotExist(repo.findByNif(nif), "There is already a mechanic with that nif");

		Mechanic m = new Mechanic(nif, name);

		repo.add(m);

		dto.id = m.getId();
		dto.version = m.getVersion();

		return dto;
	}

}
