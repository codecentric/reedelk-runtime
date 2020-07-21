package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.Mandatory;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServerObject extends AbstractOpenApiSerializable {

    // @Mandatory (https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#serverObject)
    private String url;
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
        Mandatory.check("url", url);

        Map<String, Object> serverObject = new LinkedHashMap<>();
        set(serverObject, "url", url);
        set(serverObject, "description", description);
        set(serverObject, "variables", variables, context);
        return serverObject;
    }
}

