package uo.ri.cws.application.service.paymentmean.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.util.exception.BusinessException;

public class PaymentMeanCrudServiceImpl implements PaymentMeanCrudService {

    @Override
    public CardDto addCard(CardDto card) throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public VoucherDto addVoucher(VoucherDto voucher)
            throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deletePaymentMean(String id) throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<PaymentMeanDto> findById(String id)
            throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<PaymentMeanDto> findPaymentMeansByClientNif(String nif)
            throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<VoucherDto> findVoucherByCode(String code)
            throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<CardDto> findCreditCardByNumber(String cardNumber)
            throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
