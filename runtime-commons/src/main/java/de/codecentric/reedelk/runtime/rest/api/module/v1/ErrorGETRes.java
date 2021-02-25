package de.codecentric.reedelk.runtime.rest.api.module.v1;

public class ErrorGETRes {

    private String stacktrace;
    private String message;

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
