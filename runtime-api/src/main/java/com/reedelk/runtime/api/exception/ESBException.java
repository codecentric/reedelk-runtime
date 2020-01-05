package com.reedelk.runtime.api.exception;

import com.reedelk.runtime.api.message.Message;

public class ESBException extends RuntimeException {

    private Message message;

    public ESBException() {
    }

    public ESBException(String message) {
        super(message);
    }

    public ESBException(Message message) {
        this.message = message;
    }

    public ESBException(Throwable cause) {
        super(cause);
    }

    public ESBException(String message, Throwable exception) {
        super(message, exception);
    }

    @Override
    public String toString() {
        return message != null ? message.toString() : super.toString();
    }
}
