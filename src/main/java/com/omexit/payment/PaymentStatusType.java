package com.omexit.payment;

/**
 * Created by Antony on 2/10/2016.
 */
public enum PaymentStatusType {
    INVALID_ID(0, "INVALID_ID"),
    PAYMENT_COMPLETE(1, "PAYMENT_COMPLETE"),
    PAYMENT_PENDING(2, "PAYMENT_PENDING"),
    PAYMENT_ON_HOLD(3, "PAYMENT_ON_HOLD"),
    PAYMENT_CANCELLED(4, "PAYMENT_CANCELLED"),
    PAYMENT_REVERSED(5, "PAYMENT_REVERSED"),
    PAYMENT_FAILED(6, "PAYMENT_FAILED"),
    PAYMENT_PROCESSING(7, "PAYMENT_PROCESSING");

    private final Integer value;
    private final String code;

    PaymentStatusType(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public static PaymentStatusType fromInt(final Integer typeValue) {
        PaymentStatusType enumeration = PaymentStatusType.INVALID_ID;
        switch (typeValue) {
            case 1:
                enumeration = PaymentStatusType.PAYMENT_COMPLETE;
                break;
            case 2:
                enumeration = PaymentStatusType.PAYMENT_PENDING;
                break;
            case 3:
                enumeration = PaymentStatusType.PAYMENT_ON_HOLD;
                break;
            case 4:
                enumeration = PaymentStatusType.PAYMENT_CANCELLED;
                break;
            case 5:
                enumeration = PaymentStatusType.PAYMENT_REVERSED;
                break;
            case 6:
                enumeration = PaymentStatusType.PAYMENT_FAILED;
                break;
            case 7:
                enumeration = PaymentStatusType.PAYMENT_PROCESSING;
                break;
        }

        return enumeration;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    public boolean hasTypeOf(final PaymentStatusType type) {
        return this.value.equals(type.getValue());
    }

    public boolean isInvalId() {
        return this.value.equals(PaymentStatusType.INVALID_ID.getValue());
    }

    public boolean isPaymentComplete() {
        return this.value.equals(PaymentStatusType.PAYMENT_COMPLETE.getValue());
    }

    public boolean isPaymentPending() {
        return this.value.equals(PaymentStatusType.PAYMENT_PENDING.getValue());
    }

    public boolean isPaymentOnHold() {
        return this.value.equals(PaymentStatusType.PAYMENT_ON_HOLD.getValue());
    }

    public boolean isPaymentCancelled() {
        return this.value.equals(PaymentStatusType.PAYMENT_CANCELLED.getValue());
    }

    public boolean isPaymentReversed() {
        return this.value.equals(PaymentStatusType.PAYMENT_REVERSED.getValue());
    }

    public boolean isPaymentFailed() {
        return this.value.equals(PaymentStatusType.PAYMENT_FAILED.getValue());
    }

    public boolean isPaymentProcessing() {
        return this.value.equals(PaymentStatusType.PAYMENT_PROCESSING.getValue());
    }
}
