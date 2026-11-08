package uo.ri.cws.infrastructure.persistence.jpa.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.query.PaymentMeanQueries;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.cws.domain.Client;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class PaymentMeanJpaQueries implements PaymentMeanQueries {

	@Override
	public boolean hasCharges(String paymentMeanId) {
		Long count = Jpa.getManager().createNamedQuery("PaymentMean.hasCharges", Long.class)
				.setParameter(1, paymentMeanId).getSingleResult();

		return count > 0;
	}

	@Override
	public List<VoucherSummaryDto> getVouchersSummary() {
		List<Object[]> rows = Jpa.getManager().createNamedQuery("Voucher.getSummary", Object[].class).getResultList();

		List<VoucherSummaryDto> result = new ArrayList<>();

		for (Object[] row : rows) {
			Client client = (Client) row[0];
			long issuedQty = (Long) row[1];

			BigDecimal consumedAmount = (BigDecimal) row[2];
			BigDecimal availableAmount = (BigDecimal) row[3];

			BigDecimal totalAmount = consumedAmount.add(availableAmount);

			VoucherSummaryDto dto = new VoucherSummaryDto(client.getNif().nif(), client.getFullName().name(),
					client.getFullName().surname(), issuedQty, totalAmount, availableAmount, consumedAmount);

			result.add(dto);
		}

		return result;
	}

}
