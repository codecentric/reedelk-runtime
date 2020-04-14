package com.reedelk.platform.test.utils;

import com.reedelk.runtime.commons.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ComponentsBuilder {

    public static JSONArray createNextComponentsArray(String... componentsNames) {
        JSONArray nextComponents = new JSONArray();
        for (String componentName : componentsNames) {
            nextComponents.put(ComponentsBuilder.forComponent(componentName)
                    .with("stringProperty", "Test")
                    .with("longProperty", 3L)
                    .build());
        }
        return nextComponents;
    }

    public static Builder forComponent(Class<?> componentClazz) {
        Builder builder = new Builder();
        builder.with(JsonParser.Implementor.name(), componentClazz.getName());
        return builder;
    }

    public static Builder create() {
        return new Builder();
    }

    public static Builder forComponent(String componentName) {
        Builder builder = new Builder();
        builder.with(JsonParser.Implementor.name(), componentName);
        return builder;
    }

    public static class Builder {

        private Map<String, Object> componentProperties = new HashMap<>();

        public Builder with(String propertyName, Object propertyValue) {
            componentProperties.put(propertyName, propertyValue);
            return this;
        }

        public JSONObject build() {
            JSONObject componentDefinition = new JSONObject();
            for (Map.Entry<String, Object> entry : componentProperties.entrySet()) {
                componentDefinition.put(entry.getKey(), entry.getValue());
            }
            return componentDefinition;
        }

    }
}
