package de.codecentric.reedelk.platform.commons;

public class EnterpriseOnlyModule {

    private static final String LICENSE_PACKAGE = "de.codecentric.reedelk.license";

    public static boolean is(Exception exception) {
        String message = exception.getMessage();
        return message != null && message.contains(LICENSE_PACKAGE);
    }
}
