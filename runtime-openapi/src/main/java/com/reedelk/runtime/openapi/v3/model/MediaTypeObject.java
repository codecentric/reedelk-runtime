package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class MediaTypeObject extends AbstractOpenApiSerializable {

    private ExampleReference example;
    private SchemaReference schema;

    public ExampleReference getExample() {
        return example;
    }

    public void setExample(ExampleReference example) {
        this.example = example;
    }

    public SchemaReference getSchema() {
        return schema;
    }

    public void setSchema(SchemaReference schema) {
        this.schema = schema;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> schemaMap = new LinkedHashMap<>();
        set(schemaMap, "$ref", context.schemaReference(schema));

        Map<String, Object> map = new LinkedHashMap<>();
        set(map, "schema", schemaMap);
        return map;
    }
}

/**
 * Example:
 * {
 *   "application/json": {
 *     "schema": {
 *          "$ref": "#/components/schemas/Pet"
 *     },
 *     "examples": {
 *       "cat" : {
 *         "summary": "An example of a cat",
 *         "value":
 *           {
 *             "name": "Fluffy",
 *             "petType": "Cat",
 *             "color": "White",
 *             "gender": "male",
 *             "breed": "Persian"
 *           }
 *       },
 *       "dog": {
 *         "summary": "An example of a dog with a cat's name",
 *         "value" :  {
 *           "name": "Puma",
 *           "petType": "Dog",
 *           "color": "Black",
 *           "gender": "Female",
 *           "breed": "Mixed"
 *         },
 *       "frog": {
 *           "$ref": "#/components/examples/frog-example"
 *         }
 *       }
 *     }
 *   }
 * }
 */
