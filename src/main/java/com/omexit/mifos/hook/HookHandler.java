package com.omexit.mifos.hook;

import com.omexit.util.exceptions.ValidationException;

/**
 * Created by Antony on 5/27/2016.
 */
public interface HookHandler {
    void handleHook(final String payload) throws ValidationException;
}
