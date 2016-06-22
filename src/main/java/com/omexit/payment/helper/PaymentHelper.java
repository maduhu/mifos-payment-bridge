package com.omexit.payment.helper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Antony on 4/5/2016.
 */
public interface PaymentHelper {
    @POST("./")
    Call<PaymentResult> postPaymentRequest(@Body PaymentRequest request);

    @POST("./")
    Call<Void> postCallbackACK(@Body PaymentAck request);
}
