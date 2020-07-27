package com.reedelk.runtime.openapi.v3;

import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public interface OpenApiSerializable {

    int JSON_INDENT_FACTOR = 2;

    /**
     * Open API serialize.
     */
    Map<String,Object> serialize(OpenApiSerializableContext context);

    /**
     * Open API deserialize.
     */
    void deserialize(Map<String,Object> serialized);

    /**
     * Serializes the open API object map to JSON.
     */
    default String toJson(OpenApiSerializableContext context) {
        Map<String, Object> serialized = serialize(context);
        // We use the custom object factory to preserve position
        // of serialized properties in the map.
        JSONObject jsonObject = JsonObjectFactory.newJSONObject();
        serialized.forEach(jsonObject::put);
        return jsonObject.toString(JSON_INDENT_FACTOR);
    }

    /**
     * Serializes the open API object map to YAML.
     */
    default String toYaml(OpenApiSerializableContext context) {
        Map<String, Object> serialized = serialize(context);
        Yaml yaml = new Yaml();
        return yaml.dump(serialized);
    }
}
