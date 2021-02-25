package de.codecentric.reedelk.runtime.converter;

import org.json.JSONArray;
import org.json.JSONObject;

public interface DeserializerConverter {

    static DeserializerConverter getInstance() {
        return DeserializerConverterDefault.INSTANCE;
    }

    default boolean isPrimitive(Class<?> clazz) {
        return isPrimitive(clazz.getName());
    }

    boolean isPrimitive(String fullyQualifiedName);

    // Method to create Component's types which do not require any
    // JSON Component definition, e.g the ModuleId type.
    default <T> T convert(Class<T> clazz) {
        return convert(clazz, null, null);
    }

    default <T> T convert(Class<T> expectedClass, JSONObject jsonObject, String propertyName) {
        return convert(expectedClass, jsonObject, propertyName, null);
    }

    <T> T convert(Class<T> expectedClass, JSONObject jsonObject, String propertyName, DeserializerConverterContext context);

    default <T> T convert(Class<T> expectedClass, JSONArray jsonArray, int index) {
        return convert(expectedClass, jsonArray, index, null);
    }

    <T> T convert(Class<T> expectedClass, JSONArray jsonArray, int index, DeserializerConverterContext context);
}
