package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestBodyObject extends AbstractOpenApiSerializable {

    private Boolean required;
    private String description;
    private Map<String, MediaTypeObject> content = new HashMap<>();

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, MediaTypeObject> getContent() {
        return content;
    }

    public void setContent(Map<String, MediaTypeObject> content) {
        this.content = content;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> map = new LinkedHashMap<>();
        set(map, "description", description);
        // By specification, the content object must be present, even if it is empty.
        // Therefore we add an empty object instead.
        if (content.isEmpty()) {
            map.put("content", new LinkedHashMap<>());
        }
        set(map, "content", content, context);
        set(map, "required", required);
        return map;
    }
}
