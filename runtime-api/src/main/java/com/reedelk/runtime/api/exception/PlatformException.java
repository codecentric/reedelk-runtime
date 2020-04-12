package com.reedelk.runtime.api.exception;

import com.reedelk.runtime.api.message.Message;

public class PlatformException extends RuntimeException {

    private Message message;

    public PlatformException() {
    }

    public PlatformException(String message) {
        super(message);
    }

    public PlatformException(Message message) {
        this.message = message;
    }

    public PlatformException(Throwable cause) {
        super(cause);
    }

    public PlatformException(String message, Throwable exception) {
        super(message, exception);
    }

    @Override
    public String toString() {
        return message != null ? message.toString() : super.toString();
    }
}
