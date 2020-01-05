package com.reedelk.esb.exception;

import com.reedelk.runtime.api.exception.ESBException;

public class FlowStopException extends ESBException {
    public FlowStopException(String message, Exception exception) {
        super(message, exception);
    }
}
