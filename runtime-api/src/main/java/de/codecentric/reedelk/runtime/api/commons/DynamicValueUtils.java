package de.codecentric.reedelk.runtime.api.commons;

import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

public class DynamicValueUtils {

    /**
     * Checks if the given dynamic value is null or empty.
     */
    public static <T extends DynamicValue<?>> boolean isNullOrBlank(T dynamicValue) {
        return !isNotNullOrBlank(dynamicValue);
    }

    /**
     * Checks if the given dynamic value is not null and not empty: i.e the dynamic
     * value is defined and if it is a string checks if it is not blank.
     */
    public static <T extends DynamicValue<?>> boolean isNotNullOrBlank(T dynamicValue) {
        if (dynamicValue == null) {
            return false;
        } else if (dynamicValue.isScript()) {
            return ScriptUtils.isNotBlank(dynamicValue.body());
        } else if (dynamicValue instanceof DynamicString) {
            return StringUtils.isNotBlank(((DynamicString) dynamicValue).value());
        } else if (dynamicValue.value() instanceof String){
            // A DynamicObject might have a String value (when it is not a Dynamic expression).
            // Therefore we might have dynamic values not with type DynamicString having a
            // static string value.
            return StringUtils.isNotBlank((String) dynamicValue.value());
        } else {
            return dynamicValue.value() != null;
        }
    }
}
