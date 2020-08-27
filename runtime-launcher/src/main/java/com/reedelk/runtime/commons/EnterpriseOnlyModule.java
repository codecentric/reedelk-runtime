package com.reedelk.runtime.commons;

public class EnterpriseOnlyModule {

    private static final String LICENSE_PACKAGE = "com.reedelk.license";

    public static boolean is(Exception exception) {
        String message = exception.getMessage();
        return message != null && message.contains(LICENSE_PACKAGE);
    }
}
