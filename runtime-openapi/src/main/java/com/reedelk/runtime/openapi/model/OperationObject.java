package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.openapi.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.JsonObjectFactory;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = OperationObject.class, scope = ServiceScope.PROTOTYPE)
public class OperationObject extends AbstractOpenApiSerializable implements Implementor {

    @Property("Exclude this resource from the OpenAPI document")
    @Description("Excludes this endpoint from being published in the OpenAPI document.")
    private Boolean exclude;

    @Property("Deprecated operation")
    @DefaultValue("false")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("Declares this operation to be deprecated. Consumers SHOULD refrain from usage of the declared operation.")
    private Boolean deprecated;
    
    @Property("Summary")
    @Hint("Updates orders")
    @Example("Updates an order")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("A short summary of what the operation does.")
    private String summary;

    @Property("Description")
    @Hint("Updates an order in the store with JSON data")
    @Example("Updates an order in the store with JSON data")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("A verbose explanation of the operation behavior.")
    private String description;

    @Property("Operation ID")
    @Hint("updateOrder")
    @Example("updateOrder")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("Unique string used to identify the operation. The id MUST be unique among all operations described in the API. " +
            "Tools and libraries MAY use the operationId to uniquely identify an operation, therefore, " +
            "it is RECOMMENDED to follow common programming naming conventions.")
    private String operationId;

    @Property("Request")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private RequestBodyObject requestBody;

    @Property("Responses")
    @KeyName("Status Code")
    @ValueName("Response")
    @TabGroup("Parameters Definitions and Tags")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private Map<String, ResponseObject> responses = new HashMap<>();

    @Property("Parameters")
    @TabGroup("Parameters Definitions and Tags")
    @ListDisplayProperty("name")
    @DialogTitle("Parameter Configuration")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private List<ParameterObject> parameters = new ArrayList<>();

    @Property("Tags")
    @Hint("Tag name")
    @DialogTitle("Tag")
    @TabGroup("Parameters Definitions and Tags")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private List<String> tags = new ArrayList<>();

    public Boolean getExclude() {
        return exclude;
    }

    public void setExclude(Boolean exclude) {
        this.exclude = exclude;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public RequestBodyObject getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBodyObject requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, ResponseObject> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, ResponseObject> responses) {
        this.responses = responses;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public List<ParameterObject> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterObject> parameters) {
        this.parameters = parameters;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        setList(serialized, "tags", tags);
        set(serialized, "summary", summary);
        set(serialized, "description", description);
        set(serialized, "operationId", operationId);
        set(serialized, "parameters", parameters, context);
        if (responses.isEmpty()) {
            // make sure at least one default response is present if there are
            // no user defined responses.
            responses.put("default", new ResponseObject());
        }
        set(serialized, "responses", responses, context);
        return serialized;
    }
}
