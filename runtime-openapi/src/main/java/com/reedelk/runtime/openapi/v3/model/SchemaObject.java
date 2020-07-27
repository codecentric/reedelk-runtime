package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import com.reedelk.runtime.openapi.v3.SchemaSerializer;

import java.util.Map;

public class SchemaObject extends AbstractOpenApiSerializable {

    private SchemaReference schema;

    public SchemaReference getSchema() {
        return schema;
    }

    public void setSchema(SchemaReference schema) {
        this.schema = schema;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext context) {
        return SchemaSerializer.serialize(context, schema);
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {

    }
}
