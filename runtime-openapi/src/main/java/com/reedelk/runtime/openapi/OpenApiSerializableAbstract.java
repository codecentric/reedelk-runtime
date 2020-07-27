package com.reedelk.runtime.openapi;

import com.reedelk.runtime.openapi.v3.Example;
import com.reedelk.runtime.openapi.v3.Schema;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class OpenApiSerializableAbstract implements OpenApiSerializable {

    private static final String JSON_PROPERTY_EXAMPLE = "example";

    protected void set(Map<String, Object> parent, Schema schema, OpenApiSerializableContext context) {
        set(parent, "schema", schema.serialize(context));
    }

    protected void set(Map<String, Object> parent, Example example) {
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
