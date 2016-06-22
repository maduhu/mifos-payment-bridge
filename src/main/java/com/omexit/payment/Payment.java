package com.omexit.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.omexit.channel.Channel;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Antony on 2/9/2016.
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "payment_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = IncomingPayment.class, name = "incoming"),
        @JsonSubTypes.Type(value = OutgoingPayment.class, name = "outgoing")})
@Entity(name = "tbl_payment")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonProperty("date_created")
    @Column(name = "date_created", updatable = false)
    private Date dateCreated;
    @JsonProperty("last_modified")
    @Column(name = "last_modified")
    private Date lastModified;
    @JsonProperty("tenant_id")
    @Column(name = "tenant_id")
    private String tenantId;
    @JsonProperty("client_id")
    @Column(name = "client_id")
    private Long clientId;
    @JsonProperty("entity")
    @Column(name = "entity", length = 50)
    private String entity;
    @JsonProperty("entity_id")
    @Column(name = "entity_id")
    private Long entityId;
    @JsonProperty("action")
    @Column(name = "action", length = 50)
    private String action;
    @JsonProperty("payment_account")
    @Column(name = "payment_account", length = 100)
    private String paymentAccount;
    @JsonProperty("transaction_amount")
    @Column(name = "transaction_amount")
    private Double transactionAmount;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;
    @Transient
    @JsonProperty("channel_name")
    private String channelName;
    @JsonProperty("payment_status")
    @Column(name = "payment_status")
    PaymentStatusType paymentStatus;
    @Transient
    @JsonProperty("payment_type")
    PaymentType paymentType;
    @JsonProperty("status_reason_code")
    @Column(name = "status_reason_code")
    private Long statusReasonCode;
    @JsonProperty("status_reason_code_message")
    @Column(name = "status_reason_code_message")
    private String statusReasonCodeMessage;
    @JsonProperty("currency")
    @Column(name = "currency")
    private String currency;
    @JsonProperty("external_id")
    @Column(name = "external_id", length = 50)
    private String externalId;

    @PrePersist
    protected void onCreate() {
        Date date = new Date();
        dateCreated = date;
        lastModified = date;
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = new Date();
    }
}
