package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.openapi.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.JsonObjectFactory;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.Map;

@Component(service = ServerObject.class, scope = ServiceScope.PROTOTYPE)
public class ServerObject extends AbstractOpenApiSerializable implements Implementor {

    @Property("URL")
    @Hint("https://development.gigantic-server.com/v1")
    @Example("https://development.gigantic-server.com/v1")
    @DefaultValue("/")
    @Description(" A URL to the target host. This URL supports Server Variables and MAY be relative, " +
            "to indicate that the host location is relative to the location where the OpenAPI document is being served. " +
            "Variable substitutions will be made when a variable is named in <i>{brackets}</i>.")
    private String url = "/"; // Default server URL: https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md#schema

    @Property("Description")
    @Hint("Development server")
    @Example("Development server")
    @Description("An optional string describing the host designated by the URL.")
    private String description;

    @Property("URL Variables")
    @TabGroup("URL Variables")
    @KeyName("Variable Name")
    @ValueName("Variable Definition")
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
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        set(serialized, "url", url);
        set(serialized, "description", description);
        set(serialized, "variables", variables, context);
        return serialized;
    }
}
