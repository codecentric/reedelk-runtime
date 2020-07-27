package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.Schema;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OpenApiSerializableContext {

    private static final String COMPONENTS_SCHEMA_REF_TEMPLATE = "#/components/schemas/%s";

    private final Map<String, Schema> SCHEMA_MAP = new HashMap<>();

    String schemaReference(Schema schema) {
        return String.format(COMPONENTS_SCHEMA_REF_TEMPLATE, schema.getSchemaId());
    }

    public void schemaRegister(Schema schema) {
        // If it is a reference we need to register, otherwise we
        // just serialize the schema as is.
        if (schema.isReference()) {
            if (!SCHEMA_MAP.containsKey(schema.getSchemaId())) {
                SCHEMA_MAP.put(schema.getSchemaId(), schema);
            }
        }
    }

    String schemaData(Schema schema) {
        return schemaData(schema.getSchemaId());
    }

    String schemaData(String schemaId) {
        Schema reference = SCHEMA_MAP.get(schemaId);
        return reference.getSchemaData();
    }

    public Set<String> schemasIds() {
        return SCHEMA_MAP.keySet();
    }
}
