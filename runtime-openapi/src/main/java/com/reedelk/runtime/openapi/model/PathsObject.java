package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.openapi.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.JsonObjectFactory;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class PathsObject extends AbstractOpenApiSerializable {

    private Map<String, Map<RestMethod, OperationObject>> paths = new TreeMap<>();

    @Override
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject pathsObject = JsonObjectFactory.newJSONObject();
        paths.forEach((path, pathItemObject) -> {
            JSONObject operationsByPathJsonObject = JsonObjectFactory.newJSONObject();
            pathItemObject.forEach((restMethod, operationObject) ->
                    operationsByPathJsonObject.put(restMethod.name().toLowerCase(),
                            operationObject.serialize(context)));
            pathsObject.put(path, operationsByPathJsonObject);
        });
        return pathsObject;
    }
}
