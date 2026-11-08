package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class PaymentMeanJpaRepository
		extends BaseJpaRepository<PaymentMean>
		implements PaymentMeanRepository {

	@Override
    public List<PaymentMean> findPaymentMeansByClientNif(Nif nif) {
        return Jpa.getManager()
                .createNamedQuery("PaymentMean.findByNif", PaymentMean.class)
                .setParameter(1, nif)
                .getResultList();
    }

    @Override
    public List<Voucher> findVouchersByClientNif(Nif nif) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Voucher> findVoucherByCode(VoucherCode code) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<CreditCard> findCreditCardByNumber(
            CreditCardNumber number) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<PaymentMean> findByIds(Collection<String> ids) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
