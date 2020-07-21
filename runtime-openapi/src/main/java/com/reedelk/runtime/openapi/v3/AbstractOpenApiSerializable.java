package com.reedelk.runtime.openapi.v3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractOpenApiSerializable implements OpenApiSerializable {

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
}
