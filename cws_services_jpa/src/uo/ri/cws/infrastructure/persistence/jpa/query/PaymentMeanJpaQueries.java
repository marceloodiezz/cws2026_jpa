package uo.ri.cws.infrastructure.persistence.jpa.query;

import java.util.List;

import uo.ri.cws.application.query.PaymentMeanQueries;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;

public class PaymentMeanJpaQueries implements PaymentMeanQueries {

	@Override
    public boolean hasCharges(String paymentMeanId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<VoucherSummaryDto> getVouchersSummary() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
