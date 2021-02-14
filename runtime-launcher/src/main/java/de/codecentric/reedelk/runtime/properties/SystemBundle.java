package de.codecentric.reedelk.runtime.properties;

import de.codecentric.reedelk.runtime.PlatformLauncherException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class SystemBundle {

    private static final String BUNDLES_CONFIGURATION = "/system/bundles.configuration";

    public static Collection<String> all() {
        try (InputStream resourceAsStream = SystemBundle.class.getResourceAsStream(BUNDLES_CONFIGURATION);
        InputStreamReader isReader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(isReader)) {
            return bufferedReader
                    .lines()
                    .filter(SKIP_EMPTY_LINES)
                    .collect(toList());
        } catch (IOException exception) {
            throw new PlatformLauncherException("Could not read bundles configuration", exception);
        }
    }

    public static String adminConsole() {
        return "runtime-admin-console.jar"; // NAME_CONVENTION
    }

    public static String basePath() {
        return "/system/";
    }

    private static final Predicate<String> SKIP_EMPTY_LINES =
            value -> value != null && value.length() > 0;
}
