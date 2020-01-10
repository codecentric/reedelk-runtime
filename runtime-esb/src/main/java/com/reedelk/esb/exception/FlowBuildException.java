package com.reedelk.esb.exception;

import com.reedelk.runtime.api.exception.ESBException;

public class FlowBuildException extends ESBException {

    public FlowBuildException(String message, Throwable exception) {
        super(message, exception);
    }
}
