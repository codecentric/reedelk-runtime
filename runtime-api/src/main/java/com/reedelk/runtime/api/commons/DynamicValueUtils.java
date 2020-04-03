package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;

public class DynamicValueUtils {

    /**
     * Checks if the given dynamic value is not empty: i.e the dynamic
     * value is defined and if it is a string checks if it is not blank.
     */
    public static boolean isNotNullOrBlank(DynamicString dynamicString) {
        if (dynamicString == null) {
            return false;
        } else if (dynamicString.isScript()) {
            return ScriptUtils.isNotBlank(dynamicString.body());
        } else {
            return StringUtils.isNotBlank(dynamicString.value());
        }
    }
}
