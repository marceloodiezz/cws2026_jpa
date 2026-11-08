package uo.ri.cws.application.repository;

public interface PersistenceFactory {

	MechanicRepository forMechanic();
	WorkOrderRepository forWorkOrder();
	PaymentMeanRepository forPaymentMean();
	InvoiceRepository forInvoice();
	ClientRepository forClient();
	SparePartRepository forSparePart();
	VehicleRepository forVehicle();
	VehicleTypeRepository forVehicleType();

}
