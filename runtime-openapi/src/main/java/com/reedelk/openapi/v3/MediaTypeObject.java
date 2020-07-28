package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableAbstract1;
import com.reedelk.openapi.OpenApiSerializableContext1;

import java.util.LinkedHashMap;
import java.util.Map;

public class MediaTypeObject extends OpenApiSerializableAbstract1 {

    private Example example;
    private Schema schema;

    public Example getExample() {
        return example;
    }

    public void setExample(Example example) {
        this.example = example;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema, OpenApiSerializableContext1 context) {
        context.setSchema(schema);
        this.schema = schema;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext1 context) {
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
