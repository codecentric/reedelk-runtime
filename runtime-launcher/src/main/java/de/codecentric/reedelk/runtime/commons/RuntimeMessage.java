package de.codecentric.reedelk.runtime.commons;

import de.codecentric.reedelk.runtime.PlatformLauncherException;

import java.io.IOException;
import java.util.Properties;

public class RuntimeMessage extends Properties {

    private static final RuntimeMessage INSTANCE = new RuntimeMessage();

    private RuntimeMessage() {
        try {
            load(RuntimeMessage.class.getResourceAsStream("/messages.properties"));
        } catch (IOException e) {
            throw new PlatformLauncherException("Error loading runtime message properties", e);
        }
    }

    public static String message(String key, Object ...args) {
        return String.format(INSTANCE.getProperty(key), args);
    }
}
