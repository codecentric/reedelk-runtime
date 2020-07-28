package com.reedelk.openapi;

import com.reedelk.openapi.v3.OpenApiObjectAbstract;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class OpenApiDeserializer1 {

    public static OpenApiObjectAbstract from(String jsonOrYaml) {
        Yaml yaml = new Yaml();
        Map<String,Object> load = yaml.load(jsonOrYaml);
        OpenApiObjectAbstract openApiObject = new OpenApiObjectAbstract();
        openApiObject.deserialize(load);
        return openApiObject;
    }
}
