package com.reedelk.runtime.api.exception;

import com.reedelk.runtime.api.component.Implementor;

import static java.lang.String.format;

public class ConfigurationException extends PlatformException {

    public ConfigurationException(Class<? extends Implementor> implementor, String errorMessage) {
        super(formatErrorMessage(implementor, errorMessage));
    }

    public ConfigurationException(Class<? extends Implementor> implementor, String errorMessage, Exception exception) {
        super(formatErrorMessage(implementor, errorMessage), exception);
    }

    public ConfigurationException(String errorMessage) {
        super(errorMessage);
    }

    public ConfigurationException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }

    private static String formatErrorMessage(Class<? extends Implementor> implementor, String errorMessage) {
        return format("%s (%s) has a configuration error: %s",
                implementor.getSimpleName(),
                implementor.getName(),
                errorMessage);
    }
}
