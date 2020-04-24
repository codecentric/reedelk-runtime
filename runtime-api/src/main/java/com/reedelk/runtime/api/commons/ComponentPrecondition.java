package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.exception.ComponentConfigurationException;
import com.reedelk.runtime.api.exception.ComponentInputException;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

public class ComponentPrecondition {

    public static class Input {

        private Input() {
        }

        /**
         * Checks if the input object is one of the types given in the list.
         * An exception is thrown in the input object does not have one of the
         * expected types or if the input is object is null.
         */
        public static void requireTypeMatches(Class<? extends Implementor> implementor, Object input, Class<?> ...expected) {
            if (input == null) throw new ComponentInputException(implementor, input, expected);
            for (Class<?> current : expected) {
                if (current.isAssignableFrom(input.getClass())) {
                    return;
                }
            }
            throw new ComponentInputException(implementor, input, expected);
        }

        /**
         * Checks if the input object is one of the types given in the list.
         * An exception is thrown in the input object does not have one of the expected types.
         */
        public static void requireTypeMatchesOrNull(Class<? extends Implementor> implementor, Object input, Class<?> ...expected) {
            if (input == null) return; // It is ok if null.
            requireTypeMatches(implementor, input, expected);
        }
    }

    public static class Configuration {

        private Configuration() {
        }

        public static <T> T requireNotNull(Class<? extends Implementor> implementor, T object, String errorMessage) {
            if (object == null) {
                throw new ComponentConfigurationException(implementor, errorMessage);
            }
            return object;
        }

        public static String requireNotBlank(Class<? extends Implementor> implementor, String value, String errorMessage) {
            if (StringUtils.isBlank(value)) {
                throw new ComponentConfigurationException(implementor, errorMessage);
            }
            return value;
        }

        public static void requireTrue(Class<? extends Implementor> implementor, boolean expression, String errorMessage) {
            if (!expression) {
                throw new ComponentConfigurationException(implementor, errorMessage);
            }
        }

        /**
         * Checks if the given dynamic value is null, or if it is a script if it has an empty script or if it is not
         * a script it has a null value. If the dynamic value is string, it checks if the text string is empty as well.
         */
        public static <T extends DynamicValue<?>> T requireNotNullOrBlank(Class<? extends Implementor> implementor, T dynamicValue, String errorMessage) {
            if (!DynamicValueUtils.isNotNullOrBlank(dynamicValue)) {
                throw new ComponentConfigurationException(implementor, errorMessage);
            }
            return dynamicValue;
        }

        public static <T extends DynamicValue<?>> T requireNotNull(Class<? extends Implementor> implementor, T dynamicValue, String errorMessage) {
            if (dynamicValue == null) {
                throw new ComponentConfigurationException(implementor, errorMessage);
            }
            return dynamicValue;
        }
    }
}
