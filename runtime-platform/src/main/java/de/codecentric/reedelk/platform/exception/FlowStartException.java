package de.codecentric.reedelk.platform.exception;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

public class FlowStartException extends PlatformException {

    public FlowStartException(String message, Throwable exception) {
        super(message, exception);
    }
}
