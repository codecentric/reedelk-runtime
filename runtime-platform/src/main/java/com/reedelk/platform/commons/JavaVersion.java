package com.reedelk.platform.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class JavaVersion {

    private static final Logger logger = LoggerFactory.getLogger(JavaVersion.class);

    private JavaVersion() {
    }

    public static boolean isGreaterThan18() {
        String fullVersion = System.getProperty("java.version");
        return internalIsGreaterThan18(fullVersion);
    }

    static boolean internalIsGreaterThan18(String fullVersion) {
        return fullVersionOf(fullVersion)
                // First Major Releases are parsable, e.g 14, 8, 11.
                .map(versionAsInt -> versionAsInt > 8)
                .orElseGet(() -> {
                    try {
                        String version = fullVersion.split("_")[0];
                        String[] split = version.split("\\.");
                        String onlyMajor = split[0] + split[1];
                        return Integer.parseInt(onlyMajor) > 18;
                    } catch (Throwable e) {
                        logger.warn(String.format("Could not parse full Java version=[%s]", fullVersion));
                        return false;
                    }
                });
    }

    private static Optional<Integer> fullVersionOf(String fullVersion) {
        try {
            return Optional.of(Integer.parseInt(fullVersion));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
