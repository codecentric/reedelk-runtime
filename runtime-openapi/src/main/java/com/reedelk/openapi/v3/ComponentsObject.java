package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableAbstract1;
import com.reedelk.openapi.OpenApiSerializableContext1;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ComponentsObject extends OpenApiSerializableAbstract1 {

    private Map<String, SchemaObject> schemas = new HashMap<>();

    public Map<String, SchemaObject> getSchemas() {
        return schemas;
    }

    public void setSchemas(Map<String, SchemaObject> schemas) {
        this.schemas = schemas;
    }

    @Override
    public Map<String, Object> serialize(OpenApiSerializableContext1 context) {
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> schemasMap = new LinkedHashMap<>();
        schemas.forEach((schemaId, schemaObject) ->
                set(schemasMap, schemaId, schemaObject.serialize(context)));

        Map<String,Schema> collectedSchemas = context.getSchemas();

        collectedSchemas.forEach((schemaId, schema) -> {
            if (!schemas.containsKey(schemaId)) {
                // Add the schema only if it does not exists.
                set(schemasMap, schema, context);
            }
        });
        set(map,"schemas", schemasMap);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {

    }
}
