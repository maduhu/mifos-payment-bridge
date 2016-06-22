package com.omexit.payment;

import com.omexit.channel.ChannelRepository;
import com.omexit.mifos.MifosProperties;
import com.omexit.mifos.MifosServiceHelper;
import com.omexit.mifos.hook.HookHandler;
import com.omexit.mifos.hook.HookHandlerFactory;
import com.omexit.payment.outgoingHandler.OutgoingPaymentHandler;
import com.omexit.payment.outgoingHandler.OutgoingPaymentHandlerFactory;
import com.omexit.util.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by Antony on 2/12/2016.
 */
@Service
public class PaymentServiceImpl extends BasePaymentHandler implements PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    MifosProperties mifosProperties;

    @Autowired
    ChannelRepository channelRepository;

    private MifosServiceHelper mifosServiceHelper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        mifosServiceHelper = createMifosServiceHelper(mifosProperties.getEndPoint(), mifosProperties.getUsername(),
                mifosProperties.getPassword());
    }

    @Override
    public List<Payment> findAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment saveOrUpdatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment findPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Payment findPaymentIfExist(Long entityId, String tenantId, Long clientId, String entity) {
        return paymentRepository.findByEntityIdAndTenantIdAndClientIdAndEntity(entityId, tenantId, clientId, entity);
    }

    @Override
    public void processOutgoingPayment(Payment payment) {

        logger.info(String.format("Processing payment: %s", payment.getId()));
        OutgoingPaymentHandler handler = null;

        handler = OutgoingPaymentHandlerFactory.buildPaymentHandler(payment.getChannel().getChannelType(),
                mifosServiceHelper, this);
        handler.processPayment((OutgoingPayment) payment);

    }

    @Scheduled(fixedDelayString = "${processPayment.fixedDelay}")
    @Override
    public void processOutgoingPayment() {
        List<Payment> payments = paymentRepository.findTransactionsToProcess(new Date(),
                PaymentStatusType.PAYMENT_PENDING);
        // System.err.println("Hey");
        if (!payments.isEmpty()) {
            logger.info("Fetched: " + payments.size() + " outgoing records to process");

            OutgoingPaymentHandler handler = null;

            for (Payment payment : payments) {
                handler = OutgoingPaymentHandlerFactory.buildPaymentHandler(payment.getChannel().getChannelType(),
                        mifosServiceHelper, this);
                handler.processPayment((OutgoingPayment) payment);
            }

        }

    }

    @Override
    public String processIncomingPayment(IncomingPayment incomingPayment) {
        return null;
    }

    @Override
    public void handleHook(String entity, String payload) throws ValidationException {
        HookHandler hookHandler = HookHandlerFactory.buildHookHandler(entity, this, channelRepository, mifosServiceHelper);

        hookHandler.handleHook(payload);
    }

//    @Override
//    @Transactional
//    public void handleHook(final String entity, final String action, final String payload) {
//        try {
//
//            logger.debug(String.format("handleHook(entity-%s, action-%s, payload-%s)", action, entity, payload));
//            ObjectMapper mapper = new ObjectMapper();
//
//            if (entity.equals("LOAN")) {
//                // Convert payload json string to java object
//                LoanHook loanHook = mapper.readValue(payload, LoanHook.class);
//
//                OutgoingPayment payment = (OutgoingPayment) findPaymentIfExist(loanHook.getLoanId(), loanHook.getTenantId(),
//                        loanHook.getClientId(), loanHook.getEntity());
//
//                if (payment == null) {
//                    logger.debug(String.format("findPaymentIfExist(LoanId-%s,TenantId-%s,ClientId-%sEntity-%s) - Not found, system will create new object",
//                            loanHook.getLoanId(), loanHook.getTenantId(),
//                            loanHook.getClientId(), loanHook.getEntity()));
//                    payment = new OutgoingPayment();
//                }
//                logger.debug(String.format("findPaymentIfExist(LoanId-%s,TenantId-%s,ClientId-%sEntity-%s) - Found.",
//                        loanHook.getLoanId(), loanHook.getTenantId(),
//                        loanHook.getClientId(), loanHook.getEntity()));
//
//                payment.setAction(loanHook.getAction());
//                payment.setClientId(loanHook.getClientId());
//                payment.setEntity(loanHook.getEntity());
//                payment.setEntityId(loanHook.getLoanId());
//                payment.setTenantId(loanHook.getTenantId());
//                payment.setPaymentStatus(PaymentStatusType.PAYMENT_PENDING);
//                payment.setPaymentType(PaymentType.OUTGOING);
//
//                // Get loan details from mifos if action is DISBURSE
//                if (action.equals("DISBURSE")) {
//
//                    // Check if disbursal is complete or processing, if so throw
//                    // Exception
//                    if (payment.getPaymentStatus().isPaymentComplete()) {
//                        throw new Exception(String.format("Disbursal for: %s transaction is already complete!",
//                                loanHook.getResourceId()));
//                    } else if (payment.getPaymentStatus().isPaymentProcessing()) {
//                        throw new Exception(String.format(
//                                "Disbursal for: %s transaction is still processing! Wait for final status.",
//                                loanHook.getResourceId()));
//                    }
//
//                    Map<String, String> queryParams = new HashMap<>();
//                    queryParams.put("tenantIdentifier", payment.getTenantId());
//                    queryParams.put("pretty", "false");
//                    queryParams.put("associations", "all");
//                    queryParams.put("exclude", "guarantors");
//
//                    logger.info(String.format("getLoan(%s, %s)", payment.getEntityId(), queryParams.toString()));
//                    Call<Loan> loanCall = mifosServiceHelper.getLoan(payment.getEntityId(), queryParams);
//                    Response<Loan> loanResponse = null;
//                    loanResponse = loanCall.execute();
//
//                    logger.info(String.format("getLoan() - response {:isSuccess %s :statusCode %s, :message %s}",
//                            loanResponse.isSuccess(), loanResponse.code(), loanResponse.message()));
//
//                    Loan loan = loanResponse.body();
//                    if (loan == null) {
//                        String message = "Could not find details for loan id: " + payment.getEntity()
//                                + " was not found";
//                        payment.setStatusReasonCode(ReasonCodes.EXPECTED_MIFOS_RESOURCE_NOT_FOUND);
//                        payment.setStatusReasonCodeMessage(message);
//                        payment.setPaymentStatus(PaymentStatusType.PAYMENT_FAILED);
//                        saveOrUpdatePayment(payment);
//
//                        logger.warn(message);
//                        throw new ValidationException(message, payment.getEntity());
//                    }
//
//                    // Get disbursal transaction
//                    Transaction disburseTransaction = null;
//                    for (Transaction transaction : loan.getTransactions()) {
//                        if (transaction.getType().getCode().equals("loanTransactionType.disbursement")) {
//                            disburseTransaction = transaction;
//                            break;
//                        }
//                    }
//
//                    // Check the disbursal transaction is not present if so
//                    // register
//                    // payment failure due to no disbursal transaction
//                    if (disburseTransaction == null) {
//                        String message = "Could not find disbursal transaction for loan id: " + payment.getEntity()
//                                + " was not found";
//                        payment.setStatusReasonCode(ReasonCodes.EXPECTED_MIFOS_RESOURCE_NOT_FOUND);
//                        payment.setStatusReasonCodeMessage(message);
//                        payment.setPaymentStatus(PaymentStatusType.PAYMENT_FAILED);
//                        saveOrUpdatePayment(payment);
//
//                        logger.warn(message);
//                        throw new ValidationException(message, payment.getEntity());
//                    }
//
//                    // Get amount from disbursal transaction
//                    payment.setTransactionAmount(disburseTransaction.getAmount());
//
//                    // Get channel name from disbursal transaction
//                    String channelName = disburseTransaction.getPaymentDetailData().getPaymentType().getName();
//
//                    // Get channel from registered channels
//                    Channel channel = channelRepository.findByChannelName(channelName);
//
//                    // Check if registered channel is null, if so register
//                    // payment
//                    // failure due to missing channel
//                    if (channel == null) {
//                        String message = "Channel with name: " + channelName + " not found";
//                        payment.setStatusReasonCode(ReasonCodes.RESOURCE_NOT_FOUND);
//                        payment.setStatusReasonCodeMessage(message);
//                        payment.setPaymentStatus(PaymentStatusType.PAYMENT_FAILED);
//                        saveOrUpdatePayment(payment);
//                        throw new ResourceNotFoundException(message);
//                    }
//                    payment.setChannel(channel);
//
//                    payment.setPaymentStatus(PaymentStatusType.PAYMENT_PENDING);
//                    payment.setActualDisbursementDate(DateUtil.parseDate(
//                            loanHook.getChanges().getActualDisbursementDate(), loanHook.getChanges().getDateFormat()));
//                    payment.setStatusReasonCodeMessage("OK");
//                    payment.setStatusReasonCode(ReasonCodes.OK);
//                    payment = (OutgoingPayment) saveOrUpdatePayment(payment);
//
//                    // processOutgoingPayment(payment);
//
//                } else if (action.equals("DISBURSALUNDO")) {
//
//                    if (payment.getId() != null) {
//                        // Check if disbursal is complete or processing, if so
//                        // throw
//                        // Exception
//                        if (payment.getPaymentStatus().isPaymentComplete()) {
//                            throw new Exception(String.format("Disbursal for: %s transaction is already complete!",
//                                    loanHook.getResourceId()));
//                        }
//                        //Set that payment has been put on hold
//                        payment.setPaymentStatus(PaymentStatusType.PAYMENT_ON_HOLD);
//                        payment.setStatusReasonCodeMessage("Payment put on hold.");
//                        payment.setStatusReasonCode(ReasonCodes.OK);
//                        saveOrUpdatePayment(payment);
//                    }
//                }
//            }else  if(entity.equals("SAVINGSACCOUNT")){ //If savings account
//
//                // Convert payload json string to java object
//                SavingsAccountHook savingsAccountHook = mapper.readValue(payload, SavingsAccountHook.class);
//
//                OutgoingPayment payment = (OutgoingPayment) findPaymentIfExist(savingsAccountHook.getResourceId(),
//                        savingsAccountHook.getTenantId(),
//                        savingsAccountHook.getClientId(), savingsAccountHook.getEntity());
//
//                if (payment == null) {
//                    logger.debug(String.format("findPaymentIfExist(ResourceId-%s,TenantId-%s,ClientId-%sEntity-%s) - Not found, system will create new object",
//                            savingsAccountHook.getResourceId(), savingsAccountHook.getTenantId(),
//                            savingsAccountHook.getClientId(), savingsAccountHook.getEntity()));
//                    payment = new OutgoingPayment();
//                }
//                logger.debug(String.format("findPaymentIfExist(ResourceId-%s,TenantId-%s,ClientId-%sEntity-%s) - Found.",
//                        savingsAccountHook.getResourceId(), savingsAccountHook.getTenantId(),
//                        savingsAccountHook.getClientId(), savingsAccountHook.getEntity()));
//
//                payment.setAction(savingsAccountHook.getAction());
//                payment.setClientId(savingsAccountHook.getClientId());
//                payment.setEntity(savingsAccountHook.getEntity());
//                payment.setEntityId(savingsAccountHook.getResourceId());
//                payment.setTenantId(savingsAccountHook.getTenantId());
//                payment.setPaymentStatus(PaymentStatusType.PAYMENT_PENDING);
//                payment.setPaymentType(PaymentType.OUTGOING);
//
//                //Check if action is withdrawal, if so process it accordingly
//                if (action.equals("WITHDRAWAL")) {
//// Check if disbursal is complete or processing, if so throw
//                    // Exception
//                    if (payment.getPaymentStatus().isPaymentComplete()) {
//                        throw new Exception(String.format("Disbursal for: %s transaction is already complete!",
//                                savingsAccountHook.getResourceId()));
//                    } else if (payment.getPaymentStatus().isPaymentProcessing()) {
//                        throw new Exception(String.format(
//                                "Disbursal for: %s transaction is still processing! Wait for final status.",
//                                savingsAccountHook.getResourceId()));
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
//        }
//    }

}
