package com.omexit.payment.outgoingHandler;

import com.omexit.payment.OutgoingPayment;

/**
 * Created by Antony on 2/11/2016.
 */
public interface OutgoingPaymentHandler {
    void processPayment(OutgoingPayment  outgoingPayment);
}
