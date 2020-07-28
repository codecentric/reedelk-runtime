package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableAbstract;
import com.reedelk.openapi.OpenApiSerializableContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class SchemaObject extends OpenApiSerializableAbstract {

    private Schema schema;

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema, OpenApiSerializableContext context) {
        context.setSchema(schema);
        this.schema = schema;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> map = new LinkedHashMap<>();
        set(map, schema, context);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {
        // TODO: Here
    }
}
