package com.omexit.payment.outgoingHandler;

import com.omexit.channel.ChannelClassificationType;
import com.omexit.mifos.MifosServiceHelper;
import com.omexit.payment.PaymentService;

/**
 * Created by Antony on 2/11/2016.
 */
public class OutgoingPaymentHandlerFactory {

    public static OutgoingPaymentHandler buildPaymentHandler(ChannelClassificationType channelClassificationType,
                                                             MifosServiceHelper mifosServiceHelper,  PaymentService paymentService)
    {
        OutgoingPaymentHandler paymentHandler = null;
        switch (channelClassificationType)
        {
            case BANKING_CHANNEL:
                paymentHandler = new BankingPaymentPaymentHandler();
                break;
            case EMAIL_MONEY_CHANNEL:
                paymentHandler = new EmailPaymentPaymentHandler();
                break;
            case MOBILE_MONEY_CHANNEL:
                paymentHandler = new MobilePaymentPaymentHandler(mifosServiceHelper, paymentService);
                break;
            default:
                //throw some exception
                break;
        }
        return paymentHandler;
    }
}
