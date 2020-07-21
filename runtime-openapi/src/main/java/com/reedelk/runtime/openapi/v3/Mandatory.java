package com.reedelk.runtime.openapi.v3;

public class Mandatory {

    public static void check(String propertyName, String value) {
        if (value == null) {
            throw new IllegalArgumentException(String.format("Property '%s' is mandatory", propertyName));
        }
    }
}
