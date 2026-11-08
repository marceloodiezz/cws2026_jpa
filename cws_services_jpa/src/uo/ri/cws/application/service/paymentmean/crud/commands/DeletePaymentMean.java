package uo.ri.cws.application.service.paymentmean.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.query.PaymentMeanQueries;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeletePaymentMean implements Command<Void> {

	private String id;

	private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

	private PaymentMeanQueries paymentMeanQueries = Factories.query.forPaymentMean();

	public DeletePaymentMean(String id) {
		ArgumentChecks.isNotBlank(id);
		this.id = id;
	}

	@Override
	public Void execute() throws BusinessException {
		PaymentMean paymentMean = paymentMeanRepo.findById(id).orElse(null);
		BusinessChecks.isNotNull(paymentMean, "Payment mean does not exist");
		BusinessChecks.isFalse(paymentMean instanceof Cash, "Cash payment means cannot be deleted");
		BusinessChecks.isFalse(paymentMeanQueries.hasCharges(id), "Payment mean has associated charges");

		Associations.Holds.unlink(paymentMean);

		paymentMeanRepo.remove(paymentMean);

		return null;
	}
}
