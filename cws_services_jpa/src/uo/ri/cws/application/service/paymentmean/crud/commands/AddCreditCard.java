package uo.ri.cws.application.service.paymentmean.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddCreditCard implements Command<CardDto> {

	private CardDto dto;

	private ClientRepository clientRepo = Factories.repository.forClient();

	private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

	public AddCreditCard(CardDto dto) {
		ArgumentChecks.isNotNull(dto);
		ArgumentChecks.isNotBlank(dto.clientId);
		ArgumentChecks.isNotNull(dto.cardExpiration);
		ArgumentChecks.isNotBlank(dto.cardType);
		ArgumentChecks.isTrue(CreditCardNumber.isValid(dto.cardNumber), "Invalid credit card number");

		this.dto = dto;
	}

	@Override
	public CardDto execute() throws BusinessException {
		Client client = clientRepo.findById(dto.clientId).orElse(null);
		BusinessChecks.isNotNull(client, "Client does not exist");

		CreditCardNumber number = new CreditCardNumber(dto.cardNumber);
		BusinessChecks.doesNotExist(paymentMeanRepo.findCreditCardByNumber(number), "Credit card already exists");

		CreditCard card = new CreditCard(number, dto.cardType, dto.cardExpiration);
		BusinessChecks.isTrue(card.isValidNow(), "Credit card is expired");

		Associations.Holds.link(client, card);

		paymentMeanRepo.add(card);

		return DtoAssembler.toDto(card);
	}
}
