package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServerObject extends AbstractOpenApiSerializable {

    private String url = "/"; // Default server URL: https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md#schema
    private String description;
    private Map<String, ServerVariableObject> variables;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, ServerVariableObject> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, ServerVariableObject> variables) {
        this.variables = variables;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> map = new LinkedHashMap<>();
        set(map, "url", url);
        set(map, "description", description);
        set(map, "variables", variables, context);
        return map;
    }
}
