package uo.ri.cws.application.service.paymentmean.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.cws.application.service.paymentmean.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddVoucher implements Command<VoucherDto> {

	private VoucherDto dto;

	private ClientRepository clientRepo = Factories.repository.forClient();

	private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

	public AddVoucher(VoucherDto dto) {
		ArgumentChecks.isNotNull(dto);
		ArgumentChecks.isNotBlank(dto.clientId);
		ArgumentChecks.isNotBlank(dto.code);
		ArgumentChecks.isNotBlank(dto.description);
		ArgumentChecks.isNotNull(dto.availableAmount);
		ArgumentChecks.isTrue(dto.availableAmount.signum() >= 0, "Available amount cannot be negative");

		this.dto = dto;
	}

	@Override
	public VoucherDto execute() throws BusinessException {
		Client client = clientRepo.findById(dto.clientId).orElse(null);
		BusinessChecks.isNotNull(client, "Client does not exist");

		VoucherCode code = new VoucherCode(dto.code);
		BusinessChecks.doesNotExist(paymentMeanRepo.findVoucherByCode(code), "Voucher already exists");

		Voucher voucher = new Voucher(code, dto.description, Money.of(dto.availableAmount));

		Associations.Holds.link(client, voucher);

		paymentMeanRepo.add(voucher);

		return DtoAssembler.toDto(voucher);
	}
}
