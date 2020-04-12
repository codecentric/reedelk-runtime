package com.reedelk.esb.exception;

import com.reedelk.runtime.api.exception.PlatformException;

public class ModuleDeserializationException extends PlatformException {

    public ModuleDeserializationException(String message, Exception exception) {
        super(message, exception);
    }
}
