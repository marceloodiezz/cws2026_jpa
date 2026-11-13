package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.query.ClientQueries;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.repository.RecommendationRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.Recommendation;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteClientByNif implements Command<Void> {

	private Nif nif;

	private ClientRepository clientRepo = Factories.repository.forClient();
	private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();
	private RecommendationRepository recommendationRepo = Factories.repository.forRecommendation();

	private ClientQueries query = Factories.query.forClient();

	public DeleteClientByNif(String nif) {
		ArgumentChecks.isNotBlank(nif, "The client nif cannot be blank");

		this.nif = new Nif(nif);
	}

	@Override
	public Void execute() throws BusinessException {
		Optional<Client> oc = clientRepo.findByNif(nif);

		BusinessChecks.exists(oc, "The client does not exist");

		Client client = oc.get();

		BusinessChecks.isTrue(client.getVehicles().isEmpty(), "The client has vehicles and cannot be deleted");
		BusinessChecks.isFalse(query.hasPaidInvoices(client.getId()),
				"The client has paid invoices and cannot be deleted");

		for (PaymentMean paymentMean : client.getPaymentMeans()) {
			Associations.Holds.unlink(paymentMean);
			paymentMeanRepo.remove(paymentMean);
		}

		for (Recommendation recommendation : client.getRecommendations()) {
			Associations.Recommends.unlink(recommendation);
			recommendationRepo.remove(recommendation);
		}

		Recommendation received = client.getRecommendation();
		if (received != null) {
			Associations.Recommends.unlink(received);
			recommendationRepo.remove(received);
		}

		clientRepo.remove(client);

		return null;
	}

}
