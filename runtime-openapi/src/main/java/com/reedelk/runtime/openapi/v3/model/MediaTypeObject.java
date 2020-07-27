package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.JsonSchemaUtils;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import org.json.JSONObject;

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
        Map<String, Object> map = new LinkedHashMap<>();

        // Set the schema
        if (schema != null) {
            JsonSchemaUtils.setSchema(context, map, schema);
        }

        // Set the example
        if (example != null) {
            set(map, "example", new JSONObject(example.data()).toMap());
        }

        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {

    }
}

/*
 * Media Type: Example:
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
