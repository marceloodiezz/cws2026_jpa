package uo.ri.cws.application.service.paymentmean.voucher.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.cws.application.service.paymentmean.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindVoucherByCode implements Command<Optional<VoucherDto>> {

	private String code;

	private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

	public FindVoucherByCode(String code) {
		ArgumentChecks.isNotBlank(code);
		this.code = code;
	}

	@Override
	public Optional<VoucherDto> execute() throws BusinessException {
		VoucherCode voucherCode = new VoucherCode(code);

		return paymentMeanRepo.findVoucherByCode(voucherCode).map(DtoAssembler::toDto);
	}
}
