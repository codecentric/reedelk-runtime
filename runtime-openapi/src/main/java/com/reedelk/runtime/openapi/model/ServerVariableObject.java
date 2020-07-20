package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.openapi.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.JsonObjectFactory;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.List;

import static java.util.Optional.ofNullable;

@Component(service = ServerVariableObject.class, scope = ServiceScope.PROTOTYPE)
public class ServerVariableObject extends AbstractOpenApiSerializable implements Implementor {

    @Property("Description")
    @Hint("Hosts variable")
    @Example("Hosts variable")
    @Description("An optional description for the server variable.")
    private String description;

    @Property("Default")
    @DefaultValue("default")
    @Hint("development")
    @Example("development")
    @Description("The default value to use for substitution, and to send, if an alternate value is not supplied. " +
            "Unlike the Schema Object's default, this value MUST be provided by the consumer.")
    private String defaultValue;

    @TabGroup("Variable Enum Options")
    @Property("Variable Enum Options")
    @Description("An enumeration of string values to be used if the substitution options are from a limited set.")
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
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        set(serialized, "default", ofNullable(defaultValue).orElse("default")); // REQUIRED
        set(serialized, "description", description);
        setList(serialized, "enum", enumValues);
        return serialized;
    }
}
