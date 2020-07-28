package com.reedelk.openapi;

import com.reedelk.openapi.v3.OpenApiObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class OpenApiDeserializer {

    public static OpenApiObject from(String jsonOrYaml) {
        Yaml yaml = new Yaml();
        Map<String,Object> openApiMap = yaml.load(jsonOrYaml);
        OpenApiObject openApiObject = new OpenApiObject();
        openApiObject.deserialize(openApiMap);
        return openApiObject;
    }
}
