package com.reedelk.openapi;

import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class OpenApiSerializer {

    private static final int JSON_INDENT_FACTOR = 2;

    /**
     * Serializes the open API object map to JSON.
     */
    public static String toJson(OpenApiSerializable1 serializable, OpenApiSerializableContext1 context) {
        Map<String, Object> serialized = serializable.serialize(context);
        // We use the custom object factory to preserve position
        // of serialized properties in the map.
        JSONObject jsonObject = JsonObjectFactory.newJSONObject();
        serialized.forEach(jsonObject::put);
        return jsonObject.toString(JSON_INDENT_FACTOR);
    }

    /**
     * Serializes the open API object map to YAML.
     */
    public static String toYaml(OpenApiSerializable1 serializable, OpenApiSerializableContext1 context) {
        Map<String, Object> serialized = serializable.serialize(context);
        Yaml yaml = new Yaml();
        return yaml.dump(serialized);
    }
}
