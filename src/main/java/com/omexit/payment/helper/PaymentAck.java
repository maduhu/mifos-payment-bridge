package com.omexit.payment.helper;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Antony on 2/18/2016.
 */
public class PaymentAck {
    @JsonProperty("external_id")
    private String externalId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;

}
