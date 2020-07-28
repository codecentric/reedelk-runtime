package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableAbstract1;
import com.reedelk.openapi.OpenApiSerializableContext1;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class PathsObject extends OpenApiSerializableAbstract1 {

    private Map<String, Map<RestMethod, OperationObject>> paths = new TreeMap<>();

    public Map<String, Map<RestMethod, OperationObject>> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, Map<RestMethod, OperationObject>> paths) {
        this.paths = paths;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext1 context) {
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

    @SuppressWarnings("unchecked")
    @Override
    public void deserialize(Map<String, Object> serialized) {
        serialized.forEach((pathEntry, pathDefinition) -> {
            Map<String,Object> pathDefinitionMap = (Map<String, Object>) pathDefinition;
            // TODO: This is wrong multiple for same method.
            pathDefinitionMap.forEach((method, operationObjectMap) -> {
                RestMethod restMethod = RestMethod.valueOf(method.toUpperCase());
                OperationObject operationObject = new OperationObject();
                operationObject.deserialize((Map<String,Object>) operationObjectMap);
                Map<RestMethod, OperationObject> methodAndOperationMap = new HashMap<>();
                methodAndOperationMap.put(restMethod, operationObject);
                paths.put(pathEntry, methodAndOperationMap);
            });
        });
    }
}
