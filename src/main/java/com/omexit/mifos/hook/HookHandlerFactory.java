package com.omexit.mifos.hook;

import com.omexit.channel.ChannelRepository;
import com.omexit.mifos.MifosServiceHelper;
import com.omexit.payment.PaymentService;

/**
 * Created by Antony on 5/27/2016.
 */
public class HookHandlerFactory {

    public static HookHandler buildHookHandler(String entity, PaymentService paymentService,
                                               ChannelRepository channelRepository,
                                               MifosServiceHelper mifosServiceHelper) {
        HookHandler hookHandler = null;

        switch (entity) {
            case "LOAN":
                hookHandler = new LoanHookHandler(paymentService, channelRepository, mifosServiceHelper);
                break;
            case "SAVINGSACCOUNT":
                hookHandler = new SavingsAccountHookHandler(paymentService, channelRepository, mifosServiceHelper);
                break;
            default:
                //TODO: throw some exception
                break;
        }
        return hookHandler;
    }
}
