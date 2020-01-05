package com.reedelk.runtime.api.commons;

public class ConfigurationPropertyUtils {

    public static boolean isConfigProperty(Object value) {
        return value instanceof String && isConfigProperty((String) value);
    }

    public static boolean isConfigProperty(String value) {
        if (value != null) {
            String trimmedValue = value.trim();
            return trimmedValue.startsWith("${") && value.endsWith("}");
        }
        return false;
    }

    // Remove:$[ and ] from the  beginning and the end of the text
    public static String unwrap(String value) {
        return isConfigProperty(value) ?
                value.substring(2, value.length() - 1) :
                value;
    }
}
