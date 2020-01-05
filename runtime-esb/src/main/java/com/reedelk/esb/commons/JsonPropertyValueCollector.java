package com.reedelk.esb.commons;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Recursively traverses the JSON object and collect all the property values,
 * having as property name the provided property name.
 */
public class JsonPropertyValueCollector {

    private final String propertySearchFor;

    public JsonPropertyValueCollector(String propertyName) {
        this.propertySearchFor = propertyName;
    }

    public Collection<String> collect(Collection<JSONObject> root) {
        return collectComponentNames(root);
    }

    private Collection<String> collectComponentNames(Collection<JSONObject> jsonStructure) {
        Set<String> componentNames = new HashSet<>();
        for (JSONObject jsonObject : jsonStructure) {
            componentNames.addAll(collectComponentNames(jsonObject));
        }
        return componentNames;
    }

    private Collection<String> collectComponentNames(JSONObject jsonObject) {
        Set<String> componentNames = new HashSet<>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String propertyName = keys.next();
            Object propertyObject = jsonObject.get(propertyName);
            if (propertyName.equals(propertySearchFor)) {
                String componentId = (String) propertyObject;
                componentNames.add(componentId);
            }
            if (propertyObject instanceof JSONObject) {
                componentNames.addAll(collectComponentNames((JSONObject) propertyObject));
            } else if (propertyObject instanceof JSONArray) {
                componentNames.addAll(collectComponentNames((JSONArray) propertyObject));
            }
        }
        return componentNames;
    }

    private Set<String> collectComponentNames(JSONArray jsonArray) {
        Set<String> componentNames = new HashSet<>();
        for (Object object : jsonArray) {
            if (object instanceof JSONObject) {
                componentNames.addAll(collectComponentNames((JSONObject) object));
            } else if (object instanceof JSONArray) {
                componentNames.addAll(collectComponentNames((JSONArray) object));
            }
        }
        return componentNames;
    }
}
