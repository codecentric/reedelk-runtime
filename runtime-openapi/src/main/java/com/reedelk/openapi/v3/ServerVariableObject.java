package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableAbstract1;
import com.reedelk.openapi.OpenApiSerializableContext1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class ServerVariableObject extends OpenApiSerializableAbstract1 {

    private String description;
    private String defaultValue;
    private List<String> enumValues;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(List<String> enumValues) {
        this.enumValues = enumValues;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext1 context) {
        Map<String, Object> map = new LinkedHashMap<>();
        set(map, "default", ofNullable(defaultValue).orElse("default")); // REQUIRED
        set(map, "description", description);
        setList(map, "enum", enumValues);
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deserialize(Map<String, Object> serialized) {
        description = getString(serialized, "description");
        defaultValue = getString(serialized, "defaultValue");
        enumValues = (List<String>) serialized.get("enumValues");
    }
}
