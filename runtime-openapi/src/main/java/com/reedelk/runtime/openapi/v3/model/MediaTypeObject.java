package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class MediaTypeObject extends AbstractOpenApiSerializable {

    private ExampleReference example;
    private Schema schema;

    public ExampleReference getExample() {
        return example;
    }

    public void setExample(ExampleReference example) {
        this.example = example;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema, OpenApiSerializableContext context) {
        context.schemaRegister(schema);
        this.schema = schema;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> map = new LinkedHashMap<>();
        set(map, schema, context);
        set(map, example);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {
        // TODO: Here test how to deserialize schema and example
    }
}
