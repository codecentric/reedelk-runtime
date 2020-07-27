package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.OpenApiObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class Deserializer {

    public static OpenApiObject from(String jsonOrYaml) {
        Yaml yaml = new Yaml();
        Map<String,Object> load = yaml.load(jsonOrYaml);
        OpenApiObject openApiObject = new OpenApiObject();
        openApiObject.deserialize(load);
        return openApiObject;
    }
}
