package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.ExampleReference;
import com.reedelk.runtime.openapi.v3.model.Schema;
import org.json.JSONObject;

import java.util.*;

public abstract class AbstractOpenApiSerializable implements OpenApiSerializable {

    private static final String JSON_PROPERTY_EXAMPLE = "example";
    private static final String JSON_PROPERTY_SCHEMA = "schema";
    private static final String JSON_PROPERTY_REF = "$ref";

    // Sets the following structure in the parent:
    // schema {
    //      "$ref": "#/components/schemas/mySchema"
    // }
    protected void set(Map<String, Object> parent, Schema schema, OpenApiSerializableContext context) {
        Optional.ofNullable(schema).ifPresent(theSchema -> {
            if (theSchema.isReference()) {
                Map<String, Object> schemaReferenceObject = new LinkedHashMap<>();
                schemaReferenceObject.put(JSON_PROPERTY_REF, context.schemaReference(schema));
                parent.put(JSON_PROPERTY_SCHEMA, schemaReferenceObject);
            } else {
                // TODO: Should we use YAML parser instead?
                parent.put(JSON_PROPERTY_SCHEMA, new JSONObject(schema.getSchemaData()).toMap());
            }
        });
    }

    protected void set(Map<String, Object> parent, ExampleReference example) {
        set(parent, JSON_PROPERTY_EXAMPLE, new JSONObject(example.data()).toMap());
    }

    protected void set(Map<String,Object> object, String propertyName, Map<String, ? extends OpenApiSerializable> serializableMap, OpenApiSerializableContext context) {
        if (serializableMap != null && !serializableMap.isEmpty()) {
            Map<String, Object> map = new LinkedHashMap<>();
            serializableMap.forEach((key, mapObject) -> set(map, key, mapObject, context));
            set(object, propertyName, map);
        }
    }

    protected void set(Map<String,Object> object, String propertyName, List<? extends OpenApiSerializable> serializableList, OpenApiSerializableContext context) {
        if (serializableList != null && !serializableList.isEmpty()) {
            List<Map<String,Object>> listOfItems = new ArrayList<>();
            serializableList.forEach(serializable -> listOfItems.add(serializable.serialize(context)));
            object.put(propertyName, listOfItems);
        }
    }

    protected void set(Map<String,Object> object, String propertyName, OpenApiSerializable serializable, OpenApiSerializableContext context) {
        if (serializable != null) {
            object.put(propertyName, serializable.serialize(context));
        }
    }

    protected void setList(Map<String,Object> object, String propertyName, List<String> items) {
        if (items != null && !items.isEmpty()) {
            object.put(propertyName, items);
        }
    }

    protected void set(Map<String,Object> object, String propertyName, Boolean aBoolean) {
        if (aBoolean != null) {
            object.put(propertyName, aBoolean);
        }
    }

    protected void set(Map<String,Object> object, String propertyName, Map<String,Object> value) {
        if (value != null) {
            object.put(propertyName, value);
        }
    }

    protected void set(Map<String,Object> object, String propertyName, String value) {
        if (value != null) {
            object.put(propertyName, value);
        }
    }

    protected String getString(Map<String,Object> data, String propertyName) {
        return (String) data.get(propertyName);
    }

    protected Boolean getBoolean(Map<String,Object> data, String propertyName) {
        return (Boolean) data.get(propertyName);
    }

    @SuppressWarnings("unchecked")
    protected Map<String,Object> getMap(Map<String,Object> data, String propertyName) {
        return (Map<String,Object>) data.get(propertyName);
    }

    @SuppressWarnings("unchecked")
    protected List<Map<String,Object>> getList(Map<String,Object> data, String propertyName) {
        return (List<Map<String,Object>>) data.get(propertyName);
    }
}
