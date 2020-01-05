package com.reedelk.runtime.api.exception;

public class ConfigurationException extends ESBException {

    public ConfigurationException(String errorMessage) {
        super(errorMessage);
    }

    public ConfigurationException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
