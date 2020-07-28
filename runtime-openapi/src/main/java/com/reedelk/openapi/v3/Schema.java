package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableAbstract;
import com.reedelk.openapi.OpenApiSerializableContext;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Schema extends OpenApiSerializableAbstract {

    private static final List<String> PROPERTIES_TO_EXCLUDE_FROM_SCHEMA = Arrays.asList("$id", "$schema", "name");

    private static final String COMPONENTS_SCHEMA_REF_TEMPLATE = "#/components/schemas/%s";
    private static final String JSON_PROPERTY_REF = "$ref";

    private String schemaId;
    private String schemaData;

    public Schema(String schemaId, String schemaData) {
        this.schemaId = schemaId;
        this.schemaData = schemaData;
    }

    public Schema(String schemaData) {
        this.schemaData = schemaData;
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
        if (isReference()) {
            Map<String, Object> schemaReferenceObject = new LinkedHashMap<>();
            schemaReferenceObject.put(JSON_PROPERTY_REF, String.format(COMPONENTS_SCHEMA_REF_TEMPLATE, schemaId));
            return schemaReferenceObject;
        } else {
            return serialize(schemaData);
        }
    }

    // Sets the following structure in the parent:
    // schema {
    //      "$ref": "#/components/schemas/mySchema"
    // }
    @Override
    public void deserialize(Map<String, Object> serialized) {
        if (serialized.containsKey(JSON_PROPERTY_REF)) {
            this.schemaId = getString(serialized, JSON_PROPERTY_REF);
        } else {
            // TODO: What if it is YAML?
            this.schemaData = new JSONObject(serialized).toString();
        }
    }

    private static Map<String, Object> serialize(String jsonSchema) {
        JSONObject schemaAsJsonObject = new JSONObject(jsonSchema);
        PROPERTIES_TO_EXCLUDE_FROM_SCHEMA.forEach(propertyName ->
                removePropertyIfExists(schemaAsJsonObject, propertyName));
        return schemaAsJsonObject.toMap();
    }

    private static void removePropertyIfExists(JSONObject jsonObject, String propertyName) {
        if (jsonObject.has(propertyName)) {
            jsonObject.remove(propertyName);
        }
    }
}
