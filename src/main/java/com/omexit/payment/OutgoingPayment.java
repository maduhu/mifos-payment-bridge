package com.omexit.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by Antony on 2/9/2016.
 */
@Data
@Entity(name = "outgoing")
@EqualsAndHashCode(callSuper=false)
public class OutgoingPayment extends Payment{

    @JsonProperty("actual_disbursement_date")
    @Column(name = "actual_disbursement_date")
    private Date actualDisbursementDate;


    public PaymentType getPaymentType(){
        return PaymentType.OUTGOING;
    }
}
