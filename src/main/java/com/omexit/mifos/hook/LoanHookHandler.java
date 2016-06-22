package com.omexit.mifos.hook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omexit.channel.Channel;
import com.omexit.channel.ChannelRepository;
import com.omexit.mifos.MifosServiceHelper;
import com.omexit.mifos.portfolio.loan.Loan;
import com.omexit.mifos.portfolio.loan.Transaction;
import com.omexit.mifos.portfolio.loanHook.LoanHook;
import com.omexit.payment.*;
import com.omexit.util.DateUtil;
import com.omexit.util.ReasonCodes;
import com.omexit.util.exceptions.ResourceNotFoundException;
import com.omexit.util.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antony on 5/27/2016.
 */

public class LoanHookHandler implements HookHandler {
    private PaymentService paymentService;

    private ChannelRepository channelRepository;

    private MifosServiceHelper mifosServiceHelper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    LoanHookHandler(PaymentService paymentService, ChannelRepository channelRepository, MifosServiceHelper mifosServiceHelper) {
        this.paymentService = paymentService;
        this.channelRepository = channelRepository;
        this.mifosServiceHelper = mifosServiceHelper;
    }

    @Override
    public void handleHook(String payload) throws ValidationException {
        logger.debug(String.format("handleHook(payload-%s)", payload));
        ObjectMapper mapper = new ObjectMapper();

        try {

            // Convert payload json string to java object
            LoanHook loanHook = mapper.readValue(payload, LoanHook.class);

            String action = loanHook.getAction();

            OutgoingPayment payment = (OutgoingPayment) paymentService.findPaymentIfExist(loanHook.getLoanId(), loanHook.getTenantId(),
                    loanHook.getClientId(), loanHook.getEntity());

            if (payment == null) {
                logger.debug(String.format("findPaymentIfExist(LoanId-%s,TenantId-%s,ClientId-%sEntity-%s) - Not found, system will create new object",
                        loanHook.getLoanId(), loanHook.getTenantId(),
                        loanHook.getClientId(), loanHook.getEntity()));
                payment = new OutgoingPayment();
            }
            logger.debug(String.format("findPaymentIfExist(LoanId-%s,TenantId-%s,ClientId-%sEntity-%s) - Found.",
                    loanHook.getLoanId(), loanHook.getTenantId(),
                    loanHook.getClientId(), loanHook.getEntity()));

            payment.setAction(loanHook.getAction());
            payment.setClientId(loanHook.getClientId());
            payment.setEntity(loanHook.getEntity());
            payment.setEntityId(loanHook.getLoanId());
            payment.setTenantId(loanHook.getTenantId());
            payment.setPaymentStatus(PaymentStatusType.PAYMENT_PENDING);
            payment.setPaymentType(PaymentType.OUTGOING);

            // Get loan details from mifos if action is DISBURSE
            if (action.equals("DISBURSE")) {
                //Set actual disbursement date
                payment.setActualDisbursementDate(DateUtil.parseDate(loanHook.getChanges().getActualDisbursementDate(),
                        loanHook.getChanges().getDateFormat()));
                // Check if disbursal is complete or processing, if so throw
                // Exception
                if (payment.getPaymentStatus().isPaymentComplete()) {
                    throw new Exception(String.format("Disbursal for: %s transaction is already complete!",
                            loanHook.getResourceId()));
                } else if (payment.getPaymentStatus().isPaymentProcessing()) {
                    throw new Exception(String.format(
                            "Disbursal for: %s transaction is still processing! Wait for final status.",
                            loanHook.getResourceId()));
                }

                Map<String, String> queryParams = new HashMap<>();
                queryParams.put("tenantIdentifier", payment.getTenantId());
                queryParams.put("pretty", "false");
                queryParams.put("associations", "all");
                queryParams.put("exclude", "guarantors");

                logger.info(String.format("getLoan(%s, %s)", payment.getEntityId(), queryParams.toString()));
                Call<Loan> loanCall = mifosServiceHelper.getLoan(payment.getEntityId(), queryParams);
                Response<Loan> loanResponse = null;
                loanResponse = loanCall.execute();

                Loan loan = loanResponse.body();

                logger.info(String.format("getLoan() - response {:isSuccess %s :statusCode %s, :message %s}",
                        loanResponse.isSuccess(), loanResponse.code(), loanResponse.message()));

                if (loan == null) {
                    String message = "Could not find details for loan id: " + payment.getEntity()
                            + " was not found";
                    payment.setStatusReasonCode(ReasonCodes.EXPECTED_MIFOS_RESOURCE_NOT_FOUND);
                    payment.setStatusReasonCodeMessage(message);
                    payment.setPaymentStatus(PaymentStatusType.PAYMENT_FAILED);
                    paymentService.saveOrUpdatePayment(payment);

                    logger.warn(message);
                    throw new ValidationException(message, payment.getEntity());
                }

                // Get disbursal transaction
                Transaction disburseTransaction = null;
                for (Transaction transaction : loan.getTransactions()) {
                    if (transaction.getType().getCode().equals("loanTransactionType.disbursement")) {
                        disburseTransaction = transaction;
                        break;
                    }
                }

                // Check the disbursal transaction is not present if so
                // register
                // payment failure due to no disbursal transaction
                if (disburseTransaction == null) {
                    String message = "Could not find disbursal transaction for loan id: " + payment.getEntity()
                            + " was not found";
                    payment.setStatusReasonCode(ReasonCodes.EXPECTED_MIFOS_RESOURCE_NOT_FOUND);
                    payment.setStatusReasonCodeMessage(message);
                    payment.setPaymentStatus(PaymentStatusType.PAYMENT_FAILED);
                    paymentService.saveOrUpdatePayment(payment);

                    logger.warn(message);
                    throw new ValidationException(message, payment.getEntity());
                }
                //Set currency
                payment.setCurrency(loan.getCurrency().getCode());
                // Get amount from disbursal transaction
                payment.setTransactionAmount(disburseTransaction.getAmount());

                // Get channel name from disbursal transaction
                String channelName = disburseTransaction.getPaymentDetailData().getPaymentType().getName();

                // Get channel from registered channels
                Channel channel = channelRepository.findByChannelName(channelName);

                // Check if registered channel is null, if so register
                // payment
                // failure due to missing channel
                if (channel == null) {
                    String message = "Channel with name: " + channelName + " not found";
                    payment.setStatusReasonCode(ReasonCodes.RESOURCE_NOT_FOUND);
                    payment.setStatusReasonCodeMessage(message);
                    payment.setPaymentStatus(PaymentStatusType.PAYMENT_FAILED);
                    paymentService.saveOrUpdatePayment(payment);
                    throw new ResourceNotFoundException(message);
                }
                payment.setChannel(channel);

                payment.setPaymentStatus(PaymentStatusType.PAYMENT_PENDING);
                payment.setActualDisbursementDate(DateUtil.parseDate(
                        loanHook.getChanges().getActualDisbursementDate(), loanHook.getChanges().getDateFormat()));
                payment.setStatusReasonCodeMessage("OK");
                payment.setStatusReasonCode(ReasonCodes.OK);
                payment = (OutgoingPayment) paymentService.saveOrUpdatePayment(payment);

                // processOutgoingPayment(payment);

            } else if (action.equals("DISBURSALUNDO")) {

                if (payment.getId() != null) {
                    // Check if disbursal is complete or processing, if so
                    // throw
                    // Exception
                    if (payment.getPaymentStatus().isPaymentComplete()) {
                        throw new Exception(String.format("Disbursal for: %s transaction is already complete!",
                                loanHook.getResourceId()));
                    }
                    //Set that payment has been put on hold
                    payment.setPaymentStatus(PaymentStatusType.PAYMENT_ON_HOLD);
                    payment.setStatusReasonCodeMessage("Payment put on hold.");
                    payment.setStatusReasonCode(ReasonCodes.OK);
                    paymentService.saveOrUpdatePayment(payment);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
