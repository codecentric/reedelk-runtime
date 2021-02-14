package de.codecentric.reedelk.platform.commons;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

public class SerializationException extends PlatformException {

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
