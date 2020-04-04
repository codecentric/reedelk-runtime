package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.exception.ConfigurationException;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

public class ConfigurationPreconditions {

    private ConfigurationPreconditions() {
    }

    public static <T> T requireNotNull(Class<? extends Implementor> implementor, T object, String errorMessage) {
        if (object == null) {
            throw new ConfigurationException(implementor, errorMessage);
        }
        return object;
    }

    public static String requireNotBlank(Class<? extends Implementor> implementor, String value, String errorMessage) {
        if (StringUtils.isBlank(value)) {
            throw new ConfigurationException(implementor, errorMessage);
        }
        return value;
    }

    public static void requireTrue(Class<? extends Implementor> implementor, boolean expression, String errorMessage) {
        if (!expression) {
            throw new ConfigurationException(implementor, errorMessage);
        }
    }

    /**
     * Checks if the given dynamic value is null, or if it is a script if it has an empty script or if it is not
     * a script it has a null value. If the dynamic value is string, it checks if the text string is empty as well.
     */
    public static <T extends DynamicValue<?>> T requireNotNullOrBlank(Class<? extends Implementor> implementor, T dynamicValue, String errorMessage) {
        if (!DynamicValueUtils.isNotNullOrBlank(dynamicValue)) {
            throw new ConfigurationException(implementor, errorMessage);
        }
        return dynamicValue;
    }

    public static <T extends DynamicValue<?>> T requireNotNull(Class<? extends Implementor> implementor, T dynamicValue, String errorMessage) {
        if (dynamicValue == null) {
            throw new ConfigurationException(implementor, errorMessage);
        }
        return dynamicValue;
    }
}
