package com.reedelk.runtime.api.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializableUtils {

    private SerializableUtils() {
    }

    public static <T extends Serializable> HashMap<String, T> asSerializableMap(Map<String, T> original) {
        return original == null ? new HashMap<>() : new HashMap<>(original);
    }

    public static <T extends Serializable> HashMap<String, List<T>> asSerializableMapWithList(Map<String, List<T>> original) {
        return original == null ? new HashMap<>() : new HashMap<>(original);
    }

    public static <T extends Serializable> ArrayList<T> asSerializableList(List<T> original) {
        return original == null ? new ArrayList<>() : new ArrayList<>(original);
    }
}
