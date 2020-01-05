package com.reedelk.esb.exception;

import com.reedelk.runtime.api.exception.ESBException;

public class ModuleDeserializationException extends ESBException {
    public ModuleDeserializationException(String message, Exception exception) {
        super(message, exception);
    }
}
