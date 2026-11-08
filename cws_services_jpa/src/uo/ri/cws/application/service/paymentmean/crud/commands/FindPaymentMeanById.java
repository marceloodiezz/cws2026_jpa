package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindPaymentMeanById implements Command<Optional<PaymentMeanDto>> {

	private String id;

	private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

	public FindPaymentMeanById(String id) {
		ArgumentChecks.isNotBlank(id);
		this.id = id;
	}

	@Override
	public Optional<PaymentMeanDto> execute() throws BusinessException {
		Optional<PaymentMean> paymentMean = paymentMeanRepo.findById(id);

		return paymentMean.map(DtoAssembler::toDto);
	}
}
