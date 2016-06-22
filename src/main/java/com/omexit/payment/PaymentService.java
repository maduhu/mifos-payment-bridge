package com.omexit.payment;

import com.omexit.util.exceptions.ValidationException;

import java.util.List;

/**
 * Created by Antony on 2/12/2016.
 */
public interface PaymentService {
	List<Payment> findAllPayments();

	Payment saveOrUpdatePayment(Payment payment);

	Payment findPaymentById(Long id);

	Payment findPaymentIfExist(Long entityId, String tenantId, Long clientId, String entity);

	void processOutgoingPayment(Payment payment);
	
	void processOutgoingPayment();

	String processIncomingPayment(IncomingPayment incomingPayment);

//	void handleHook(final String entity, final String action,final String payload) throws ValidationException;

	void handleHook(final String entity,final String payload) throws ValidationException;
}
