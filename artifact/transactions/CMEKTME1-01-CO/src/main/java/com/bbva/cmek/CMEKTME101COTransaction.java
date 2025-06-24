package com.bbva.cmek;

import com.bbva.cmek.dto.payments.PaymentDTO;
import com.bbva.cmek.lib.rme1.CMEKRME1;
import com.bbva.elara.domain.transaction.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bill payment
 */
public class CMEKTME101COTransaction extends AbstractCMEKTME101COTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMEKTME101COTransaction.class);

    /**
     * The execute method...
     */
    @Override
    public void execute() {
        CMEKRME1 cmekRME1 = getServiceLibrary(CMEKRME1.class);
        PaymentDTO paymentDTO = getPayment();
        PaymentDTO response = cmekRME1.executeDoBillPayment(paymentDTO);
        setPayment(response);
        //
        if (!getAdviceList().isEmpty()) {
            // Report Error
            this.setSeverity(Severity.ENR);
        }
    }
}
