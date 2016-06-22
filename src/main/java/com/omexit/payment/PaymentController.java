package com.omexit.payment;

import com.omexit.channel.Channel;
import com.omexit.payment.helper.PaymentResult;
import com.omexit.util.BaseController;
import com.omexit.util.ReasonCodes;
import com.omexit.util.exceptions.ResourceNotFoundException;
import com.omexit.util.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The PaymentController class is a RESTful web service controller. The
 * <code>@RestController</code> annotation informs Spring that each
 * <code>@RequestMapping</code> method returns a <code>@ResponseBody</code>.
 *
 * @author Antony Omeri
 */
@RestController
public class PaymentController extends BaseController{
    @Autowired
    PaymentService paymentService;

    /**
     * Web service endpoint to fetch all Payment entities. The service returns the collection of Payment entity in JSON
     *
     * @return A ResponseEntity containing a Collection of Payment Objects
     */
    @RequestMapping(
            value = PAYMENT_URL,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Payment>> getPayments() {
        Collection<Payment> payments = paymentService.findAllPayments();
        
        Collection<Payment> outPayments=new ArrayList<>();
        for(Payment payment: payments){
        	Channel channel=payment.getChannel();
        	if(channel!=null){
        	payment.setChannelName(channel.getChannelName());
        	}
        	outPayments.add(payment);
        }

        return new ResponseEntity<Collection<Payment>>(outPayments, HttpStatus.OK);
    }


    /**
     * Web service endpoint to fetch a single Payment entity by primary key identifier
     * <p>
     * If found the Payment is returned as JSON with HTTP status 200
     * <p>
     * If not found, the service returns an empty response body with HTTP status 404
     *
     * @param id
     * @return A ResponseEntity containing a single Payment object,
     */
    @RequestMapping(
            value = GET_PAYMENT_URL,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> getPayments(@PathVariable Long id) throws ResourceNotFoundException {
        Payment payment = paymentService.findPaymentById(id);
        if (payment == null) {
            return new ResponseEntity<Payment>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Payment>(payment, HttpStatus.OK);
    }

    /**
     * Web service endpoint to create a single Payment entity. The HTTP request
     * body is expected to contain a Payment object in JSON format. The
     * Payment is persisted in the data repository.
     * <p>
     * If created successfully, the persisted Payment is returned as JSON with
     * HTTP status 201.
     * <p>
     * If not created successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param payment The Payment object to be created.
     * @return A ResponseEntity containing a single Payment object, if created
     * successfully, and a HTTP status code as described in the method
     * comment.
     */
    @RequestMapping(
            value = PAYMENT_URL,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment savedPayment = paymentService.saveOrUpdatePayment(payment);

        return new ResponseEntity<Payment>(savedPayment, HttpStatus.CREATED);
    }

    /**
     * Web service endpoint to update a single Payment entity. The HTTP request
     * body is expected to contain a Payment object in JSON format. The
     * Payment is updated in the data repository.
     * <p>
     * If updated successfully, the persisted Payment is returned as JSON with
     * HTTP status 200.
     * <p>
     * If not found, the service returns an empty response body and HTTP status
     * 404.
     * <p>
     * If not updated successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param paymentId     The Payment Id of the payment status.
     * @param paymentResult The Payment result
     * @return A ResponseEntity containing a single Payment object, if updated
     * successfully, and a HTTP status code as described in the method
     * comment.
     * @throws Exception Thrown if a problem occurs completing the request.
     */
//    @RequestMapping(
//            value = "/payment/{id}",
//            method = RequestMethod.PUT,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Payment> updatePayment(@RequestBody Payment payment) {
//        Payment updatedPayment = paymentService.saveOrUpdatePayment(payment);
//
//        return new ResponseEntity<Payment>(updatedPayment, HttpStatus.OK);
//    }
    @RequestMapping(
            value = PAYMENT_CALLBACK_URL,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> paymentCallback(@PathVariable Long paymentId, @RequestBody PaymentResult paymentResult) throws Exception {
        Payment payment = paymentService.findPaymentById(paymentId);
        //If payment is null Then throw validation Exception
        if (payment == null) {
            throw new ValidationException("Payment with the id: " + paymentId + " not found");
        }
        if (paymentResult.getStatus().equals("0")) {
            payment.setPaymentStatus(PaymentStatusType.PAYMENT_COMPLETE);
            payment.setStatusReasonCode(ReasonCodes.OK);
            payment.setStatusReasonCodeMessage("Payment completed");
        }else{
            payment.setPaymentStatus(PaymentStatusType.PAYMENT_FAILED);
            payment.setStatusReasonCode(ReasonCodes.OUTGOING_PAYMENT_FAILED);
            payment.setStatusReasonCodeMessage("Payment Failed");
        }

        Payment updatedPayment = paymentService.saveOrUpdatePayment(payment);

        return new ResponseEntity<Payment>(updatedPayment, HttpStatus.OK);
    }
}
