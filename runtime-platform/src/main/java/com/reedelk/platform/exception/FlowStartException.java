package com.reedelk.platform.exception;

import com.reedelk.runtime.api.exception.PlatformException;

public class FlowStartException extends PlatformException {

    public FlowStartException(String message, Throwable exception) {
        super(message, exception);
    }
}
