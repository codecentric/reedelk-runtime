package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.Schema;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SchemaSerializer {

    private static final List<String> PROPERTIES_TO_EXCLUDE_FROM_SCHEMA = Arrays.asList("$id", "$schema", "name");

    public static Map<String,Object> serialize(OpenApiSerializableContext context, Schema schema) {
        String jsonSchema = context.schemaData(schema);
        return serialize(jsonSchema);
    }

    public static Map<String,Object> serialize(OpenApiSerializableContext context, String schemaId) {
        String jsonSchema = context.schemaData(schemaId);
        return serialize(jsonSchema);
    }

    private static Map<String, Object> serialize(String jsonSchema) {
        JSONObject schemaAsJsonObject = new JSONObject(jsonSchema);
        PROPERTIES_TO_EXCLUDE_FROM_SCHEMA.forEach(propertyName ->
                removePropertyIfExists(schemaAsJsonObject, propertyName));
        return schemaAsJsonObject.toMap();
    }

    private static void removePropertyIfExists(JSONObject jsonObject, String propertyName) {
        if (jsonObject.has(propertyName)) {
            jsonObject.remove(propertyName);
        }
    }
}
