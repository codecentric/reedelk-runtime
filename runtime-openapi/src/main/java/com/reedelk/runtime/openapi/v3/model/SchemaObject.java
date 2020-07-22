package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SchemaObject extends AbstractOpenApiSerializable {

    private static final List<String> PROPERTIES_TO_EXCLUDE_FROM_SCHEMA = Arrays.asList("$id", "$schema", "name");

    private SchemaReference schema;

    public SchemaReference getSchema() {
        return schema;
    }

    public void setSchema(SchemaReference schema) {
        this.schema = schema;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext context) {

        // TODO: Complete me
        //String jsonSchema = schema.data(context);

        JSONObject schemaAsJsonObject = new JSONObject("{}");
        PROPERTIES_TO_EXCLUDE_FROM_SCHEMA.forEach(propertyName ->
                removePropertyIfExists(schemaAsJsonObject, propertyName));
        return schemaAsJsonObject.toMap();
    }

    private void removePropertyIfExists(JSONObject jsonObject, String propertyName) {
        if (jsonObject.has(propertyName)) {
            jsonObject.remove(propertyName);
        }
    }
}
