package com.reedelk.openapi;

import com.reedelk.openapi.v3.Schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OpenApiSerializableContext1 {

    private final Map<String, Schema> SCHEMAS_MAP = new HashMap<>();

    public void setSchema(Schema schema) {
        // If it is a reference we need to register, otherwise we
        // just serialize the schema as is.
        if (schema.isReference()) {
            if (!SCHEMAS_MAP.containsKey(schema.getSchemaId())) {
                SCHEMAS_MAP.put(schema.getSchemaId(), schema);
            }
        }
    }

    public Map<String, Schema> getSchemas() {
        return Collections.unmodifiableMap(SCHEMAS_MAP);
    }
}
