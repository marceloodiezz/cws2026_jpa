package uo.ri.cws.application.service.paymentmean.voucher.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.exception.BusinessException;

public class GenerateWorkOrderVouchers implements Command<Integer> {

	private ClientRepository clientRepo = Factories.repository.forClient();
	private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

	@Override
	public Integer execute() throws BusinessException {
		int generated = 0;

		List<Client> clients = clientRepo.findWithWorkOrdersUnusedForVoucher(3);

		for (Client client : clients) {
			while (client.getWorkOrdersAvailableForVoucher20().size() >= 3) {
				Voucher voucher = new Voucher(VoucherCode.generate(), "Por tres órdenes de trabajo", Money.of("20.00"));

				Associations.Holds.link(client, voucher);

				paymentMeanRepo.add(voucher);

				client.markThreeWorkOrdersAsUsedForVoucher20();

				generated++;
			}
		}

		return generated;
	}
}
