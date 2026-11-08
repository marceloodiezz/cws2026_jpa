package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindCreditCardByNumber implements Command<Optional<CardDto>> {

	private String cardNumber;

	private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

	public FindCreditCardByNumber(String cardNumber) {
		ArgumentChecks.isTrue(CreditCardNumber.isValid(cardNumber), "Invalid credit card number");
		this.cardNumber = cardNumber;
	}

	@Override
	public Optional<CardDto> execute() throws BusinessException {
		CreditCardNumber number = new CreditCardNumber(cardNumber);

		return paymentMeanRepo.findCreditCardByNumber(number).map(DtoAssembler::toDto);
	}
}
