package uo.ri.cws.application.service.paymentmean.voucher;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.voucher.commands.FindVouchersByClientNif;
import uo.ri.cws.application.service.paymentmean.voucher.commands.GenerateRecommendationVouchers;
import uo.ri.cws.application.service.paymentmean.voucher.commands.GetVoucherSummary;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class VoucherGenerationServiceImpl implements VoucherGenerationService {

	private CommandExecutor executor = Factories.executor.forExecutor();

	@Override
	public int[] generateVouchers() throws BusinessException {
		int[] counters = { 0, 0, 0 };

		counters[1] = executor.execute(new GenerateRecommendationVouchers());

		return counters;
	}

	@Override
	public List<VoucherDto> findVouchersByClientNif(String nif) throws BusinessException {
		return executor.execute(new FindVouchersByClientNif(nif));
	}

	@Override
	public List<VoucherSummaryDto> getVoucherSummary() throws BusinessException {
		return executor.execute(new GetVoucherSummary());
	}
}
