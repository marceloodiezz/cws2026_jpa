package uo.ri.cws.application.service.paymentmean.voucher;

import java.util.List;

import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.util.exception.BusinessException;

public class VoucherGenerationServiceImpl implements VoucherGenerationService {

    @Override
    public int[] generateVouchers() throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<VoucherDto> findVouchersByClientNif(String nif)
            throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<VoucherSummaryDto> getVoucherSummary()
            throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
