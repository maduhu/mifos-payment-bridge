package com.omexit.payment.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by Antony on 3/1/2016.
 */
@Data
public class PaymentRequest {
    @JsonProperty("payment_bridge_ref")
    private Long paymentBridgeRef;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("callback_url")
    private String callbackUrl;

}
