package uo.ri.cws.application.query;

public interface QueryFactory {

	MechanicQueries forMechanic();
	InvoiceQueries forInvoice();

}
