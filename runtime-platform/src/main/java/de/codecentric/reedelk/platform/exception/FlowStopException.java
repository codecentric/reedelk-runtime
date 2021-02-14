package de.codecentric.reedelk.platform.exception;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

public class FlowStopException extends PlatformException {

    public FlowStopException(String message, Exception exception) {
        super(message, exception);
    }
}
