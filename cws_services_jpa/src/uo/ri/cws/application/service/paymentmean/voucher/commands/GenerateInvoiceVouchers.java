package uo.ri.cws.application.service.paymentmean.voucher.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.exception.BusinessException;

public class GenerateInvoiceVouchers implements Command<Integer> {

    private InvoiceRepository invoiceRepo = Factories.repository.forInvoice();
    private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

    @Override
    public Integer execute() throws BusinessException {
        List<Invoice> invoices = invoiceRepo.findOverAmountUnusedForVoucher(Money.of("500.00"));

        int generated = 0;

        for (Invoice invoice : invoices) {
            if (!invoice.canGenerateVoucher30()) {
                continue;
            }

            if (invoice.getWorkOrders().isEmpty()) {
                continue;
            }

            Client client = invoice.getWorkOrders().iterator().next().getVehicle().getClient();
            Voucher voucher = new Voucher(VoucherCode.generate(), "Por factura de más de 500", Money.of("30.00"));
            Associations.Holds.link(client, voucher);

            paymentMeanRepo.add(voucher);

            invoice.markAsUsedForVoucher();

            generated++;
        }

        return generated;
    }
}
