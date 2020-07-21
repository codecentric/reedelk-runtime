package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ComponentsObject extends AbstractOpenApiSerializable {

    private Map<String, SchemaObject> schemas = new HashMap<>();

    public Map<String, SchemaObject> getSchemas() {
        return schemas;
    }

    public void setSchemas(Map<String, SchemaObject> schemas) {
        this.schemas = schemas;
    }

    @Override
    public Map<String, Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> schemasMap = new LinkedHashMap<>();
        schemas.forEach((schemaId, schemaObject) ->
                set(schemasMap, schemaId, schemaObject.serialize(context)));
        set(map,"schemas", schemasMap);
        return map;
    }
}
