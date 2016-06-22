package com.omexit.util.exceptions;

/**
 * Created by aomeri on 12/9/2015.
 */
public class SimfetchException extends Exception {
    private static final long serialVersionUID = 78676664663567750L;

    private Integer errorCode;
    private String msisdn;

    public SimfetchException(String message, Integer errorCode, String msisdn) {
        super(message);
        this.errorCode = errorCode;
        this.msisdn=msisdn;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getMsisdn() {
        return msisdn;
    }
}
