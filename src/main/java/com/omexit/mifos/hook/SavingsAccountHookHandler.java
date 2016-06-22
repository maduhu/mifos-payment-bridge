package com.omexit.mifos.hook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omexit.channel.ChannelRepository;
import com.omexit.mifos.MifosServiceHelper;
import com.omexit.mifos.portfolio.savingAccountHook.SavingsAccountHook;
import com.omexit.payment.*;
import com.omexit.util.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Antony on 5/27/2016.
 */

public class SavingsAccountHookHandler implements HookHandler {
    private PaymentService paymentService;

    private ChannelRepository channelRepository;

    private MifosServiceHelper mifosServiceHelper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    SavingsAccountHookHandler(PaymentService paymentService, ChannelRepository channelRepository, MifosServiceHelper mifosServiceHelper) {
        this.paymentService = paymentService;
        this.channelRepository = channelRepository;
        this.mifosServiceHelper = mifosServiceHelper;
    }

    @Override
    public void handleHook(String payload) throws ValidationException {
        logger.debug(String.format("handleHook(payload-%s)",payload));
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Convert payload json string to java object
            SavingsAccountHook savingsAccountHook = mapper.readValue(payload, SavingsAccountHook.class);

            String action = savingsAccountHook.getAction();

            OutgoingPayment payment = (OutgoingPayment) paymentService.findPaymentIfExist(savingsAccountHook.getResourceId(),
                    savingsAccountHook.getTenantId(),
                    savingsAccountHook.getClientId(), savingsAccountHook.getEntity());

            if (payment == null) {
                logger.debug(String.format("findPaymentIfExist(ResourceId-%s,TenantId-%s,ClientId-%sEntity-%s) - Not found, system will create new object",
                        savingsAccountHook.getResourceId(), savingsAccountHook.getTenantId(),
                        savingsAccountHook.getClientId(), savingsAccountHook.getEntity()));
                payment = new OutgoingPayment();
            }
            logger.debug(String.format("findPaymentIfExist(ResourceId-%s,TenantId-%s,ClientId-%sEntity-%s) - Found.",
                    savingsAccountHook.getResourceId(), savingsAccountHook.getTenantId(),
                    savingsAccountHook.getClientId(), savingsAccountHook.getEntity()));

            payment.setAction(savingsAccountHook.getAction());
            payment.setClientId(savingsAccountHook.getClientId());
            payment.setEntity(savingsAccountHook.getEntity());
            payment.setEntityId(savingsAccountHook.getResourceId());
            payment.setTenantId(savingsAccountHook.getTenantId());
            payment.setPaymentStatus(PaymentStatusType.PAYMENT_PENDING);
            payment.setPaymentType(PaymentType.OUTGOING);

            //Check if action is withdrawal, if so process it accordingly
            if (action.equals("WITHDRAWAL")) {
                // Check if disbursal is complete or processing, if so throw
                // Exception
                if (payment.getPaymentStatus().isPaymentComplete()) {
                    throw new Exception(String.format("Disbursal for: %s transaction is already complete!",
                            savingsAccountHook.getResourceId()));
                } else if (payment.getPaymentStatus().isPaymentProcessing()) {
                    throw new Exception(String.format(
                            "Disbursal for: %s transaction is still processing! Wait for final status.",
                            savingsAccountHook.getResourceId()));
                }
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
