package com.omexit.paymentEvent;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Antony on 2/10/2016.
 */
@Data
@Entity(name = "tbl_payment_events")
public class PaymentEvent {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "payload")
    private String payload;

    @Column(name = "processed")
    private Boolean processed;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "date_created")
    private Date dateCreated;

}
