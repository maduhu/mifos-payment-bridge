package com.omexit.util.exceptions;

/**
 * Created by aomeri on 12/9/2015.
 */
public class ResourceNotFoundException extends Exception {
    private static final long serialVersionUID = 12324443434353558L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
