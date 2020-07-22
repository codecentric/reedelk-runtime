package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.OpenApiObject;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.reedelk.runtime.openapi.v3.OpenApiSerializable.JSON_INDENT_FACTOR;
import static java.util.stream.Collectors.toList;

public class RESTListenerOpenApiSerializer {

    public static String serializeConfig(OpenApiObject openApiObject) {
        OpenApiSerializableContext context = new OpenApiSerializableContext();

        Map<String,Object> mappedConfigOpenApi = new LinkedHashMap<>();
        mappedConfigOpenApi.put("info", openApiObject.getInfo().serialize(context));
        mappedConfigOpenApi.put("components", openApiObject.getComponents().serialize(context));
        mappedConfigOpenApi.put("servers", openApiObject.getServers()
                .stream()
                .map(serverObject -> serverObject.serialize(context))
                .collect(toList()));

        // There is one object containing
        return new JSONObject(mappedConfigOpenApi).toString(JSON_INDENT_FACTOR);
    }

    class Result {
        String serializedConfigurationOpenApiObject;

    }
}
