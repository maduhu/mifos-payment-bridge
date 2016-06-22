package com.omexit.payment.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by Antony on 2/18/2016.
 */
@Data
public class PaymentResult {
    @JsonProperty("payment_bridge_ref")
    private Long paymentBridgeRef;
    @JsonProperty("external_id")
    private String externalId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("request_timestamp")
    private String requestTimestamp;
}
