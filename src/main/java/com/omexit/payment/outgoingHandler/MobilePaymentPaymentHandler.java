package com.omexit.payment.outgoingHandler;

import com.omexit.mifos.MifosServiceHelper;
import com.omexit.mifos.portfolio.client.Client;
import com.omexit.payment.BasePaymentHandler;
import com.omexit.payment.OutgoingPayment;
import com.omexit.payment.PaymentService;
import com.omexit.payment.PaymentStatusType;
import com.omexit.payment.helper.PaymentHelper;
import com.omexit.payment.helper.PaymentRequest;
import com.omexit.payment.helper.PaymentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antony on 2/11/2016.
 */
public class MobilePaymentPaymentHandler extends BasePaymentHandler implements OutgoingPaymentHandler {

    MifosServiceHelper mifosServiceHelper;
    PaymentService paymentService;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public MobilePaymentPaymentHandler(MifosServiceHelper mifosServiceHelper, PaymentService paymentService) {
        this.mifosServiceHelper = mifosServiceHelper;
        this.paymentService = paymentService;
    }

    @Transactional
    @Override
    public void processPayment(OutgoingPayment outgoingPayment) {
        PaymentResult paymentResult = null;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("tenantIdentifier", outgoingPayment.getTenantId());
        queryParams.put("pretty", "false");
        queryParams.put("fields", "id,accountNo,active,activationDate,firstname,lastname,displayName,mobileNo,dateOfBirth,middlename");
        try {
            Client client = mifosServiceHelper.getClient(outgoingPayment.getClientId(), queryParams).execute().body();

            //Create PaymentHelper
            PaymentHelper paymentHelper = createPaymentHelper(outgoingPayment.getChannel().getChannelEndpoint());

            //Create PaymentRequest
            PaymentRequest request = new PaymentRequest();
            request.setAccountNumber(client.getMobileNo());
            request.setAmount(outgoingPayment.getTransactionAmount());
            request.setCurrency(outgoingPayment.getCurrency());
            request.setPaymentBridgeRef(outgoingPayment.getId());
            request.setDescription("Mobile money payment to: " + client.getMobileNo());

            //Send request
            Call<PaymentResult> resultCall = paymentHelper.postPaymentRequest(request);
            Response<PaymentResult> paymentResultResponse = resultCall.execute();
            boolean isPaymentSucces = paymentResultResponse.isSuccess();
            int paymentResponseCode = paymentResultResponse.code();

            logger.info(String.format("Mobile money request ->(%s) to: %s, account: %s, amount: %s, endpoint: %s -> status(%s)",
                    outgoingPayment.getId(),
                    outgoingPayment.getChannel().getChannelName(), client.getMobileNo(),
                    outgoingPayment.getTransactionAmount(),
                    outgoingPayment.getChannel().getChannelEndpoint(), paymentResponseCode));

            if (isPaymentSucces) {
                paymentResult = paymentResultResponse.body();
                logger.debug("Payment result: " + paymentResult);

                if(paymentResult.getStatus().equals("1")) {
                    outgoingPayment.setPaymentStatus(PaymentStatusType.PAYMENT_PROCESSING);
                    outgoingPayment.setExternalId(paymentResult.getExternalId());
                }else {
                    outgoingPayment.setPaymentStatus(PaymentStatusType.PAYMENT_FAILED);
                }
                outgoingPayment.setStatusReasonCodeMessage(paymentResult.getMessage());
               

                logger.info(String.format("Mobile money request ->(:id %s, :externalId %s) processing ...", outgoingPayment.getId(),paymentResult.getExternalId()));
                logger.debug(String.format("Mobile money request ->(%s) processing ...", outgoingPayment.toString()));
            }else{
                logger.warn(String.format("Mobile money request ->(%s) failed with status: %s!",outgoingPayment.getId(), paymentResponseCode));
                outgoingPayment.setPaymentStatus(PaymentStatusType.PAYMENT_FAILED);
            }
            outgoingPayment.setPaymentAccount(client.getMobileNo());
            outgoingPayment=(OutgoingPayment)paymentService.saveOrUpdatePayment(outgoingPayment);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
