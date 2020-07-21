package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.OpenApiObject;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public interface OpenApiSerializable {

    int JSON_INDENT_FACTOR = 2;

    Map<String,Object> serialize(OpenApiSerializableContext context);

    default OpenApiObject fromYaml(String yamlAsString) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(yamlAsString, OpenApiObject.class);
    }

    default String toJson(OpenApiSerializableContext context) {
        Map<String, Object> serialized = serialize(context);
        return new JSONObject(serialized).toString(JSON_INDENT_FACTOR);
    }

    default String toYaml(OpenApiSerializableContext context) {
        Map<String, Object> serialized = serialize(context);
        Yaml yaml = new Yaml();
        return yaml.dump(serialized);
    }

    // REST Listener Model
    default String toRestListenerModel(OpenApiSerializableContext context) {
        // There is one object containing
        Map<String, Object> serialized = serialize(context);
        return new JSONObject(serialized).toString(JSON_INDENT_FACTOR);
    }
}
