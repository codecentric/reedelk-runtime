package com.reedelk.runtime.properties;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class SystemBundle {

    public static Collection<String> all() {
        return Collections.unmodifiableList(Arrays.asList(
                "org.osgi.util.function-1.1.0.jar",
                "org.osgi.util.promise-1.1.1.jar",
                "org.osgi.util.pushstream-1.0.1.jar",
                "org.apache.felix.configadmin-1.9.16.jar",
                "org.osgi.service.component-1.4.0.jar",
                "org.apache.felix.scr-2.1.16.jar",
                "pax-logging-api-1.11.0.jar",
                "pax-logging-logback-1.11.0.jar",
                "json-20190722.jar",
                "httpcore-osgi-4.4.13.jar",
                "httpclient-osgi-4.5.12.jar",
                "httpasyncclient-osgi-4.1.4.jar",
                "jsr305-3.0.2.jar",
                "reactive-streams-1.0.3.jar",
                "reactor-core-3.3.6.RELEASE.jar",
                "runtime-api.jar", // NAME_CONVENTION
                "runtime-commons.jar", // NAME_CONVENTION
                "runtime-platform.jar")); // NAME_CONVENTION
    }

    public static String adminConsole() {
        return "runtime-admin-console.jar"; // NAME_CONVENTION
    }

    public static String basePath() {
        return "/system/";
    }
}
