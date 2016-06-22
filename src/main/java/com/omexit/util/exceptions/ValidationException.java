package com.omexit.util.exceptions;

/**
 * Created by aomeri on 12/9/2015.
 */
public class ValidationException extends Exception {
    private static final long serialVersionUID = 786766646785999990L;
    private String entity;
    private int errorCode;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String entity) {
        super(message);
        this.entity = entity;
    }

    public ValidationException(String message, String entity,int errorCode ) {
        super(message);
        this.entity = entity;
        this.errorCode=errorCode;
    }

    public String getEntity() {
        return entity;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
