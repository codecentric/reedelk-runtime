package com.reedelk.runtime.openapi;

import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public abstract class AbstractOpenApiSerializable implements OpenApiSerializable {

    protected void set(JSONObject object, String propertyName, Map<String, ? extends OpenApiSerializable> serializableMap, OpenApiSerializableContext context) {
        if (serializableMap != null && !serializableMap.isEmpty()) {
            JSONObject serializedMapObject = JsonObjectFactory.newJSONObject();
            serializableMap.forEach((key, mapObject) -> set(serializedMapObject, key, mapObject, context));
            set(object, propertyName, serializedMapObject);
        }
    }

    protected void set(JSONObject object, String propertyName, List<? extends OpenApiSerializable> serializableList, OpenApiSerializableContext context) {
        if (serializableList != null && !serializableList.isEmpty()) {
            JSONArray array = new JSONArray();
            serializableList.forEach(serializable -> array.put(serializable.serialize(context)));
            object.put(propertyName, array);
        }
    }

    protected void set(JSONObject object, String propertyName, OpenApiSerializable serializable, OpenApiSerializableContext context) {
        if (serializable != null) {
            object.put(propertyName, serializable.serialize(context));
        }
    }

    protected void setList(JSONObject object, String propertyName, List<String> items) {
        if (items != null && !items.isEmpty()) {
            object.put(propertyName, items);
        }
    }

    protected void set(JSONObject object, String propertyName, ResourceText resource) {
        if (resource != null) {
            object.put(propertyName, StreamUtils.FromString.consume(resource.data()));
        }
    }

    protected void set(JSONObject object, String propertyName, Boolean aBoolean) {
        if (aBoolean != null) {
            object.put(propertyName, aBoolean);
        }
    }

    protected void set(JSONObject object, String propertyName, JSONObject value) {
        if (value != null) {
            object.put(propertyName, value);
        }
    }

    protected void set(JSONObject object, String propertyName, String value) {
        if (value != null) {
            object.put(propertyName, value);
        }
    }
}
