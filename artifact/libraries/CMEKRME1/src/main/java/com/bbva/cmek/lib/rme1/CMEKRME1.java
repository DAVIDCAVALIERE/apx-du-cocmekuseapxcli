package com.bbva.cmek.lib.rme1;

import com.bbva.cmek.dto.payments.PaymentDTO;

/**
 * The  interface CMEKRME1 class...
 */
public interface CMEKRME1 {

    /**
     * The execute method...
     */
    PaymentDTO executeDoBillPayment(PaymentDTO payment);

}