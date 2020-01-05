package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.exception.ConfigurationException;

public class ConfigurationPreconditions {

    private ConfigurationPreconditions() {
    }

    public static <T> T requireNotNull(T object, String errorMessage) {
        if (object == null) {
            throw new ConfigurationException(errorMessage);
        }
        return object;
    }

    public static String requireNotBlank(String value, String errorMessage) {
        if (StringUtils.isBlank(value)) {
            throw new ConfigurationException(errorMessage);
        }
        return value;
    }

    public static void requireTrue(boolean expression, String errorMessage) {
        if (!expression) {
            throw new ConfigurationException(errorMessage);
        }
    }
}
