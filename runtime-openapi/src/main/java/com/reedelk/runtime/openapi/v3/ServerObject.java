package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.OpenApiSerializableAbstract;
import com.reedelk.runtime.openapi.Precondition;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServerObject extends OpenApiSerializableAbstract {

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
        Precondition.checkNotNull("url", url);

        Map<String, Object> serverObject = new LinkedHashMap<>();
        set(serverObject, "url", url);
        set(serverObject, "description", description);
        set(serverObject, "variables", variables, context);
        return serverObject;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {
        url = getString(serialized, "url");
        description = getString(serialized, "description");
        if (serialized.containsKey("variables")) {
            Map<String, Map<String, Object>> variablesMap = (Map<String, Map<String, Object>>) serialized.get("variables");
            variablesMap.forEach((serverVariableKey, objectMap) -> {
                ServerVariableObject serverVariableObject = new ServerVariableObject();
                serverVariableObject.deserialize(objectMap);
                variables.put(serverVariableKey, serverVariableObject);
            });
        }
    }
}

