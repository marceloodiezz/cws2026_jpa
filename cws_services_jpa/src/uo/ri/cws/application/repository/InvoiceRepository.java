package uo.ri.cws.application.repository;

import java.util.Optional;

import uo.ri.cws.domain.Invoice;

public interface InvoiceRepository extends Repository<Invoice> {

	/**
	 * @param invoice number
	 * @return an optional with the invoice with the given number
	 */
	Optional<Invoice> findByNumber(Long number);
	
}
