package com.reedelk.platform.commons;

import com.reedelk.runtime.api.exception.PlatformException;

public class SerializationException extends PlatformException {

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
