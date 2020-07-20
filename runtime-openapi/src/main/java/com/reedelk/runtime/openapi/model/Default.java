package com.reedelk.runtime.openapi.model;

public enum Default {

    SUCCESS_RESPONSE("My Success Response"),
    ERROR_RESPONSE("My Error response");

    public final String message;

    Default(String message) {
        this.message = message;
    }
}
