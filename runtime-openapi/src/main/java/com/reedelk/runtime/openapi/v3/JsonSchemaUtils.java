package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.SchemaReference;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class JsonSchemaUtils {

    private static final String JSON_PROPERTY_SCHEMA = "schema";
    private static final String JSON_PROPERTY_REF = "$ref";

    private JsonSchemaUtils() {
    }

    // Sets the following structure in the parent if the predefined schema is 'NONE'.
    // schema {
    //      "$ref": "#/components/schemas/mySchema"
    // }
    // Otherwise it sets the predefined schema:
    // schema {
    //      "type": "integer"
    //      "format": "int64"
    // }
    public static void setSchema(OpenApiSerializableContext context,
                                 Map<String, Object> parent,
                                 PredefinedSchema predefinedSchema,
                                 SchemaReference schema) {
        // Custom schema
        if (PredefinedSchema.NONE.equals(predefinedSchema)) {
            setSchema(context, parent, schema);
        } else {
            parent.put(JSON_PROPERTY_SCHEMA, new JSONObject(predefinedSchema.schema()).toMap());
        }
    }

    // Sets the following structure in the parent:
    // schema {
    //      "$ref": "#/components/schemas/mySchema"
    // }
    public static void setSchema(OpenApiSerializableContext context,
                                 Map<String, Object> parent,
                                 SchemaReference schema) {
        Optional.ofNullable(schema).ifPresent(theSchema -> {
            Map<String, Object> schemaReferenceObject = new LinkedHashMap<>();
            schemaReferenceObject.put(JSON_PROPERTY_REF, context.schemaReference(schema));
            parent.put(JSON_PROPERTY_SCHEMA, schemaReferenceObject);
        });
    }
}
