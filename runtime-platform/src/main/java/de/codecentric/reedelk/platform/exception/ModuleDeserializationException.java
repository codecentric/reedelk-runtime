package de.codecentric.reedelk.platform.exception;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

public class ModuleDeserializationException extends PlatformException {

    public ModuleDeserializationException(String message, Exception exception) {
        super(message, exception);
    }
}
