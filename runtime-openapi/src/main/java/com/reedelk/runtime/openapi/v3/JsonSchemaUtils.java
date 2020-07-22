package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.SchemaReference;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class JsonSchemaUtils {

    private static final String JSON_PROPERTY_SCHEMA = "schema";
    private static final String JSON_PROPERTY_REF = "$ref";

    private JsonSchemaUtils() {
    }

    public static void setSchema(OpenApiSerializableContext context,
                                 JSONObject serialized,
                                 PredefinedSchema predefinedSchema,
                                 SchemaReference schema) {

        // Custom schema
        if (PredefinedSchema.NONE.equals(predefinedSchema)) {
            ofNullable(schema).ifPresent(theSchema -> {
                String schemaReference = context.schemaReferenceOf(theSchema);
                Map<String, Object> schemaReferenceObject = new LinkedHashMap<>();
                schemaReferenceObject.put(JSON_PROPERTY_REF, schemaReference);
                serialized.put(JSON_PROPERTY_SCHEMA, schemaReferenceObject);
            });
        } else {
            // Predefined schema
            serialized.put(JSON_PROPERTY_SCHEMA, new JSONObject(predefinedSchema.schema()));
        }
    }
}
