package uo.ri.cws.application.service.invoice.create.commands;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Money.MoneyCalculator;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class SettleInvoice implements Command<Void> {

    private String invoiceId;
    private Map<String, BigDecimal> charges;

    private InvoiceRepository invoiceRepo = Factories.repository.forInvoice();
    private PaymentMeanRepository paymentMeanRepo = Factories.repository.forPaymentMean();

    public SettleInvoice(String invoiceId, Map<String, BigDecimal> charges) {
        ArgumentChecks.isNotBlank(invoiceId);
        ArgumentChecks.isNotNull(charges);
        ArgumentChecks.isTrue(!charges.isEmpty(), "Charges cannot be empty");

        for (Map.Entry<String, BigDecimal> entry : charges.entrySet()) {
            ArgumentChecks.isNotBlank(entry.getKey());
            ArgumentChecks.isNotNull(entry.getValue());
        }

        this.invoiceId = invoiceId;
        this.charges = Map.copyOf(charges);
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<Invoice> optionalInvoice = invoiceRepo.findById(invoiceId);
        BusinessChecks.exists(optionalInvoice, "The invoice does not exist");

        Invoice invoice = optionalInvoice.get();
        BusinessChecks.isFalse(invoice.isSettled(), "The invoice is already settled");

        List<PaymentMean> paymentMeans = paymentMeanRepo.findByIds(charges.keySet());
        BusinessChecks.isTrue(paymentMeans.size() == charges.size(), "Some payment means do not exist");

        Map<PaymentMean, Money> requestedCharges = new HashMap<>();

        for (PaymentMean paymentMean : paymentMeans) {
            BigDecimal amount = charges.get(paymentMean.getId());
            requestedCharges.put(paymentMean, Money.of(amount));
        }

        MoneyCalculator calculator = Money.calc();

        for (Money amount : requestedCharges.values()) {
            calculator.add(Money.calc(amount).toMoneyRoundedToCents());
        }

        Money totalCharges = calculator.toMoneyRoundedToCents();

        BusinessChecks.isTrue(totalCharges.equals(invoice.getTotal()), "Charges total must equal invoice total");

        for (Map.Entry<PaymentMean, Money> entry : requestedCharges.entrySet()) {
            PaymentMean paymentMean = entry.getKey();
            Money amount = Money.calc(entry.getValue()).toMoneyRoundedToCents();
            BusinessChecks.isTrue(paymentMean.canBeCharged(amount), "Payment mean cannot be charged");
        }

        invoice.settle(requestedCharges);

        return null;
    }
}
