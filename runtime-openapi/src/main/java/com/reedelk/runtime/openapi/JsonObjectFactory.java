package com.reedelk.runtime.openapi;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class JsonObjectFactory {

    private static Field jsonObjectMapField;

    static {
        try {
            jsonObjectMapField = JSONObject.class.getDeclaredField("map");
            jsonObjectMapField.setAccessible(true);
        } catch (NoSuchFieldException ignored) {
            // nothing we can do.
        }
    }

    private JsonObjectFactory() {
    }

    /**
     * Instantiates a new JSON Object with an ordered HashMap. This
     * is needed so that properties are serialized in the same order
     * they are added during the serialization process.
     *
     * @return a new JSONObject instance with an internal representation
     * which keeps the order in which properties are added to the Object.
     */
    public static JSONObject newJSONObject() {
        JSONObject result = new JSONObject();
        if (jsonObjectMapField != null) {
            try {
                jsonObjectMapField.set(result, new LinkedHashMap<>());
            } catch (IllegalAccessException ignored) {
                // nothing we can do.
            }
        }
        return result;
    }
}
