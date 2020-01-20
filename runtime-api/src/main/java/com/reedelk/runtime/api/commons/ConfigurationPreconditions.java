package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.exception.ConfigurationException;

public class ConfigurationPreconditions {

    private ConfigurationPreconditions() {
    }

    @Deprecated
    public static <T> T requireNotNull(T object, String errorMessage) {
        if (object == null) {
            throw new ConfigurationException(errorMessage);
        }
        return object;
    }

    public static <T> T requireNotNull(Class<? extends Implementor> implementor, T object, String errorMessage) {
        if (object == null) {
            throw new ConfigurationException(implementor, errorMessage);
        }
        return object;
    }

    @Deprecated
    public static String requireNotBlank(String value, String errorMessage) {
        if (StringUtils.isBlank(value)) {
            throw new ConfigurationException(errorMessage);
        }
        return value;
    }

    public static String requireNotBlank(Class<? extends Implementor> implementor, String value, String errorMessage) {
        if (StringUtils.isBlank(value)) {
            throw new ConfigurationException(implementor, errorMessage);
        }
        return value;
    }

    @Deprecated
    public static void requireTrue(boolean expression, String errorMessage) {
        if (!expression) {
            throw new ConfigurationException(errorMessage);
        }
    }

    public static void requireTrue(Class<? extends Implementor> implementor, boolean expression, String errorMessage) {
        if (!expression) {
            throw new ConfigurationException(implementor, errorMessage);
        }
    }
}