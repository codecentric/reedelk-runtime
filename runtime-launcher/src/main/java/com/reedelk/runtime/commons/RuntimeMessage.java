package com.reedelk.runtime.commons;

import com.reedelk.runtime.ESBRuntimeException;

import java.io.IOException;
import java.util.Properties;

public class RuntimeMessage extends Properties {

    private static final RuntimeMessage INSTANCE = new RuntimeMessage();

    private RuntimeMessage() {
        try {
            load(RuntimeMessage.class.getResourceAsStream("/messages.properties"));
        } catch (IOException e) {
            throw new ESBRuntimeException("Error loading runtime message properties", e);
        }
    }

    public static String message(String key, Object ...args) {
        return String.format(INSTANCE.getProperty(key), args);
    }

}
