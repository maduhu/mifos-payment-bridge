package com.omexit.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.omexit.mifos.MifosServiceHelper;
import com.omexit.payment.helper.PaymentHelper;
import com.omexit.util.HttpClientHelper;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Antony on 4/5/2016.
 */
public abstract class BasePaymentHandler {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public PaymentHelper createPaymentHelper(String urlEndpoint) {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(urlEndpoint)
				.addConverterFactory(JacksonConverterFactory.create()).client(HttpClientHelper.getUnsafeOkHttpClient())
				.build();

		logger.info(String.format("createPaymentHelper - (%s)", urlEndpoint));

		return retrofit.create(PaymentHelper.class);
	}

	public MifosServiceHelper createMifosServiceHelper(String urlEndpoint, String username, String password) {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(urlEndpoint)
				.addConverterFactory(JacksonConverterFactory.create())
				.client(HttpClientHelper.getUnsafeOkHttpClient(username, password)).build();

		logger.info(String.format("createMifosServiceHelper - (%s,%s,%s)", urlEndpoint, username,
				password));

		return retrofit.create(MifosServiceHelper.class);
	}
}
