package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import com.reedelk.runtime.openapi.v3.SchemaSerializer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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

        Set<String> schemasIds = context.schemasIds();
        schemasIds.forEach(schemaId -> {
            if (!schemas.containsKey(schemaId)) {
                // Add the schema only if it does not exists. Meaning that the user
                // has not already added the schema.
                Map<String, Object> serializedSchema = SchemaSerializer.serialize(context, schemaId);
                set(schemasMap, schemaId, serializedSchema);
            }
        });

        set(map,"schemas", schemasMap);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {

    }
}
