package com.reedelk.esb.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaVersion {

    private static final Logger logger = LoggerFactory.getLogger(JavaVersion.class);

    public static boolean isGreaterThan18() {
        String fullVersion = System.getProperty("java.version");
        try {
            String version = fullVersion.split("_")[0];
            String[] split = version.split("\\.");
            String onlyMajor = split[0] + split[1];
            return Integer.parseInt(onlyMajor) > 18;
        } catch (Throwable e) {
            logger.warn("Could not parse full Java version: " + fullVersion);
            return false;
        }
    }
}
