package uo.ri.cws.application.service.invoice.create.commands;

import java.util.List;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class CreateInvoiceFor implements Command<InvoiceDto>{

	private List<String> workOrderIds;

	public CreateInvoiceFor(List<String> workOrderIds) {
		ArgumentChecks.isNotNull( workOrderIds );
		ArgumentChecks.isFalse( workOrderIds.isEmpty() );
		ArgumentChecks.isFalse(workOrderIds.stream().anyMatch(i -> i == null));
		
		this.workOrderIds = workOrderIds;
	}

	@Override
	public InvoiceDto execute() throws BusinessException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
