package de.codecentric.reedelk.platform.exception;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

public class FlowBuildException extends PlatformException {

    public FlowBuildException(String message, Throwable exception) {
        super(message, exception);
    }
}
