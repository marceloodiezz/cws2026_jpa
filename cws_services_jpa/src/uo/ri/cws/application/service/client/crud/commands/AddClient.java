package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.repository.RecommendationRepository;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Recommendation;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class AddClient implements Command<ClientDto> {

	private ClientDto client;
	private String recommenderNif;

	private ClientRepository clientRepo = Factories.repository.forClient();
	private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();
	private RecommendationRepository recommendationRepo = Factories.repository.forRecommendation();

	public AddClient(ClientDto client, String recommenderNif) {
		ArgumentChecks.isNotNull(client, "The client cannot be null");

		ArgumentChecks.isNotBlank(client.nif, "The nif cannot be blank");
		ArgumentChecks.isNotBlank(client.name, "The name cannot be blank");
		ArgumentChecks.isNotBlank(client.surname, "The surname cannot be blank");
		ArgumentChecks.isNotBlank(client.email, "The email cannot be blank");
		ArgumentChecks.isNotBlank(client.phone, "The phone cannot be blank");

		ArgumentChecks.isNotBlank(client.addressStreet, "The street cannot be blank");
		ArgumentChecks.isNotBlank(client.addressCity, "The city cannot be blank");
		ArgumentChecks.isNotBlank(client.addressCountry, "The country cannot be blank");
		ArgumentChecks.isNotBlank(client.addressZipcode, "The zipcode cannot be blank");

		if (recommenderNif != null) {
			ArgumentChecks.isNotBlank(recommenderNif, "The recommender nif cannot be blank");
		}

		this.client = client;
		this.recommenderNif = recommenderNif;
	}

	@Override
	public ClientDto execute() throws BusinessException {
		Nif nif = new Nif(client.nif);

		if (clientRepo.findByNif(nif).isPresent()) {
			throw new BusinessException("A client with this nif already exists");
		}

		if (recommenderNif != null) {
			Optional<Client> recommender = clientRepo.findByNif(new Nif(recommenderNif));

			if (recommender.isEmpty()) {
				throw new BusinessException("The recommender does not exist");
			}
		}

		Client newClient = new Client(nif, new FullName(client.name, client.surname), new Email(client.email),
				new PhoneNumber(client.phone),
				new Address(client.addressStreet, client.addressCity, client.addressCountry, client.addressZipcode));

		clientRepo.add(newClient);

		Cash cash = new Cash(newClient);
		paymentMeanRepo.add(cash);

		if (recommenderNif != null) {
			Client recommender = clientRepo.findByNif(new Nif(recommenderNif)).get();
			Recommendation recommendation = new Recommendation(recommender, newClient);
			recommendationRepo.add(recommendation);
		}

		return DtoAssembler.toDto(newClient);
	}

}
