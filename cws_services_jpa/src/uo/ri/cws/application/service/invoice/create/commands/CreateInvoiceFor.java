package uo.ri.cws.application.service.invoice.create.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.query.InvoiceQueries;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class CreateInvoiceFor implements Command<InvoiceDto> {

	private List<String> workOrderIds;

	private WorkOrderRepository workOrderRepo = Factories.repository.forWorkOrder();
	private InvoiceRepository invoiceRepo = Factories.repository.forInvoice();
	private InvoiceQueries invoiceQueries = Factories.query.forInvoice();

	public CreateInvoiceFor(List<String> workOrderIds) {
		ArgumentChecks.isNotNull(workOrderIds);
		ArgumentChecks.isFalse(workOrderIds.isEmpty());
		ArgumentChecks.isFalse(workOrderIds.stream().anyMatch(i -> i == null));

		this.workOrderIds = workOrderIds;
	}

	@Override
	public InvoiceDto execute() throws BusinessException {
		List<WorkOrder> workOrders = workOrderRepo.findByIds(workOrderIds);

		BusinessChecks.isTrue(workOrders.size() == workOrderIds.size(), "Some work orders do not exist");

		for (WorkOrder workOrder : workOrders) {
			BusinessChecks.isTrue(workOrder.isApproved(), "The work order is not approved");
		}

		Long nextNumber = invoiceQueries.getNextInvoiceNumber();
		Invoice invoice = new Invoice(nextNumber, workOrders);

		invoiceRepo.add(invoice);

		return DtoAssembler.toDto(invoice);
	}

}
