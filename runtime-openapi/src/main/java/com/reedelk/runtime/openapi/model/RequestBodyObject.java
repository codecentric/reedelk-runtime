package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.openapi.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.JsonObjectFactory;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.HashMap;
import java.util.Map;

@Collapsible
@Component(service = RequestBodyObject.class, scope = ServiceScope.PROTOTYPE)
public class RequestBodyObject extends AbstractOpenApiSerializable implements Implementor {

    @Property("Required")
    @DefaultValue("false")
    @Description("Determines if the request body is required in the request.")
    private Boolean required;

    @Property("Description")
    @Hint("My request body")
    @Description("A brief description of the request body. This could contain examples of use.")
    private String description;

    @Property("Request")
    @KeyName("Mime Type")
    @ValueName("Edit Request")
    @TabGroup("Tags and Requests")
    @DialogTitle("Request Content")
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
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        set(serialized, "description", description);
        // By specification, the content object must be present, even if it is empty.
        if (content.isEmpty()) {
            serialized.put("content", new JSONObject());
        }
        set(serialized, "content", content, context);
        set(serialized, "required", required);
        return serialized;
    }
}
