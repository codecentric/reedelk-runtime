package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.ComponentsObject;
import com.reedelk.runtime.openapi.v3.model.SchemaObject;
import com.reedelk.runtime.openapi.v3.model.SchemaReference;

import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

public class OpenApiSerializableContext {

    private static final String COMPONENTS_SCHEMA_REF_TEMPLATE = "#/components/schemas/%s";

    private ComponentsObject componentsObject;

    public OpenApiSerializableContext(ComponentsObject componentsObject) {
        this.componentsObject = componentsObject;
    }

    public String schemaReferenceOf(SchemaReference schema) {

        return findSchemaMatching(schema)
                .map(schemaId -> format(COMPONENTS_SCHEMA_REF_TEMPLATE, schemaId))
                .orElseGet(() -> {
                    // The schema needs to be added to the components object
                    // because it does not exists already.
                    Map<String, SchemaObject> schemas = componentsObject.getSchemas();
                    String schemaId = JsonSchemaUtils.findIdFrom(schema);
                    SchemaObject newSchemaObject = new SchemaObject();
                    newSchemaObject.setSchema(schema);
                    schemas.put(schemaId, newSchemaObject);
                    return format(COMPONENTS_SCHEMA_REF_TEMPLATE, schemaId);
                });
    }

    /**
     * Returns the schema ID matching the given resource file.
     */
    private Optional<String> findSchemaMatching(SchemaReference target) {
        Map<String, SchemaObject> schemas = componentsObject.getSchemas();
        // TODO: Fixme
        /**
        for (Map.Entry<String, SchemaObject> entry : schemas.entrySet()) {
            String currentSchemaId = entry.getKey();
            SchemaObject currentSchemaObject = entry.getValue();
            String path = currentSchemaObject.getSchema().path();
            if (path.equals(target.path())) {
                return Optional.of(currentSchemaId);
            }
        }*/
        return Optional.empty();
    }
}
