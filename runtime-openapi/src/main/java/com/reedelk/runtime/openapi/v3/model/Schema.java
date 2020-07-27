package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.OpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;

import java.util.Map;

public class Schema implements OpenApiSerializable {

    private final String schemaId;
    private final String schemaData;

    public Schema(String schemaId, String schemaData) {
        this.schemaId = schemaId;
        this.schemaData = schemaData;
    }

    public Schema(String schemaData) {
        this.schemaData = schemaData;
        this.schemaId = null;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public String getSchemaData() {
        return schemaData;
    }

    public boolean isReference() {
        return schemaId != null && schemaId.length() > 0;
    }

    @Override
    public Map<String, Object> serialize(OpenApiSerializableContext context) {
        return null;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {

    }
}
