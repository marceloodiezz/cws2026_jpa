package uo.ri.cws.application.service.client.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindClientsRecommendedBy implements Command<List<ClientDto>> {

	private Nif recommenderNif;

	private ClientRepository repo = Factories.repository.forClient();

	public FindClientsRecommendedBy(String recommenderNif) {
		ArgumentChecks.isNotBlank(recommenderNif, "The recommender nif cannot be blank");

		this.recommenderNif = new Nif(recommenderNif);
	}

	@Override
	public List<ClientDto> execute() throws BusinessException {
		List<Client> clients = repo.findRecommendedBy(recommenderNif);

		return DtoAssembler.toDtoList(clients);
	}

}
