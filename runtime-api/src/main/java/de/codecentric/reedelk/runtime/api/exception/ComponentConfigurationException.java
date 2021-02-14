package de.codecentric.reedelk.runtime.api.exception;

import de.codecentric.reedelk.runtime.api.component.Implementor;

import static java.lang.String.format;

public class ComponentConfigurationException extends PlatformException {

    public ComponentConfigurationException(Class<? extends Implementor> implementor, String errorMessage) {
        super(formatErrorMessage(implementor, errorMessage));
    }

    public ComponentConfigurationException(Class<? extends Implementor> implementor, String errorMessage, Exception exception) {
        super(formatErrorMessage(implementor, errorMessage), exception);
    }

    public ComponentConfigurationException(String errorMessage) {
        super(errorMessage);
    }

    public ComponentConfigurationException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }

    private static String formatErrorMessage(Class<? extends Implementor> implementor, String errorMessage) {
        return format("%s (%s) has a configuration error: %s",
                implementor.getSimpleName(),
                implementor.getName(),
                errorMessage);
    }
}
