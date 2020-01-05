package com.reedelk.esb.exception;

import com.reedelk.runtime.api.exception.ESBException;

public class FlowStartException extends ESBException {
    public FlowStartException(String message, Throwable exception) {
        super(message, exception);
    }
}
