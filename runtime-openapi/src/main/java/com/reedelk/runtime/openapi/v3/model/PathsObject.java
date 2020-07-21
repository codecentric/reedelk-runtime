package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class PathsObject extends AbstractOpenApiSerializable {

    private Map<String, Map<RestMethod, OperationObject>> paths = new TreeMap<>();

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> pathsObject = new LinkedHashMap<>();
        paths.forEach((path, pathItemObject) -> {
            Map<String, Object> operationsByPathJsonObject = new LinkedHashMap<>();
            pathItemObject.forEach((restMethod, operationObject) ->
                    operationsByPathJsonObject.put(restMethod.name().toLowerCase(),
                            operationObject.serialize(context)));
            pathsObject.put(path, operationsByPathJsonObject);
        });
        return pathsObject;
    }

    public Map<String, Map<RestMethod, OperationObject>> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, Map<RestMethod, OperationObject>> paths) {
        this.paths = paths;
    }
}
