package com.reedelk.platform.exception;

import com.reedelk.runtime.api.exception.PlatformException;

public class FlowStopException extends PlatformException {

    public FlowStopException(String message, Exception exception) {
        super(message, exception);
    }
}
