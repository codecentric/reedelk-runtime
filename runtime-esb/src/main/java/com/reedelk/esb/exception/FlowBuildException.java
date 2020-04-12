package com.reedelk.esb.exception;

import com.reedelk.runtime.api.exception.PlatformException;

public class FlowBuildException extends PlatformException {

    public FlowBuildException(String message, Throwable exception) {
        super(message, exception);
    }
}
