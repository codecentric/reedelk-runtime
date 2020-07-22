package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.SchemaReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OpenApiSerializableContext {

    private static final String COMPONENTS_SCHEMA_REF_TEMPLATE = "#/components/schemas/%s";

    private final Map<String,SchemaReference> SCHEMA_MAP = new HashMap();


    public void schemaRegister(SchemaReference schema) {
        if (!SCHEMA_MAP.containsKey(schema.getSchemaId())) {
            SCHEMA_MAP.put(schema.getSchemaId(), schema);
        }
    }

    public String schemaData(SchemaReference schema) {
        return schemaData(schema.getSchemaId());
    }

    public String schemaData(String schemaId) {
        SchemaReference reference = SCHEMA_MAP.get(schemaId);
        return reference.getSchemaData();
    }

    public Set<String> schemasIds() {
        return SCHEMA_MAP.keySet();
    }
}
