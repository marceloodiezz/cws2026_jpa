package uo.ri.cws.application.service.acceptance.demo.classicvehicles.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.InvoiceDtoBuilder;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;

public class DemoInvoiceHelper {

	private static final BigDecimal CLASSIC_SURCHARGE = new BigDecimal("1.5");
	private static final BigDecimal VAT_PERCENTAGE = new BigDecimal("21");

	public static InvoiceDto computeFor(TWorkOrdersRecord wo) {
		BigDecimal subtotal = wo.total_amount
				.multiply(CLASSIC_SURCHARGE)
				.setScale(2, RoundingMode.HALF_EVEN);

		return toDto(subtotal);
	}

	public static InvoiceDto computeFor(List<TWorkOrdersRecord> workOrders) {
		BigDecimal subtotal = workOrders.stream()
				.map(wo -> wo.total_amount)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.multiply(CLASSIC_SURCHARGE)
				.setScale(2, RoundingMode.HALF_EVEN);

		return toDto(subtotal);
	}

	private static InvoiceDto toDto(BigDecimal subtotal) {
		BigDecimal vatAmount = subtotal
				.multiply(VAT_PERCENTAGE)
				.divide(new BigDecimal("100"))
				.setScale(2, RoundingMode.HALF_EVEN);
		
		BigDecimal total = subtotal.add(vatAmount);

		return new InvoiceDtoBuilder()
			.withSubtotal(subtotal.doubleValue())
			.withVatAmount(vatAmount.doubleValue())
			.withTotal(total.doubleValue())
			.withVatPercentage(VAT_PERCENTAGE.doubleValue())
			.withState("ISSUED")
			.build();
	}

}
