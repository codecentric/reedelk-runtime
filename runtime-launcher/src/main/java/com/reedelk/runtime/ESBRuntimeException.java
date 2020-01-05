package com.reedelk.runtime;

public class ESBRuntimeException extends RuntimeException {

    public ESBRuntimeException(String message, Exception e) {
        super(message, e);
    }

    public ESBRuntimeException(String message) {
        super(message);
    }
}
