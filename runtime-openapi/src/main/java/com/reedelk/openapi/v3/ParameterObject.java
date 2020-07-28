package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableAbstract;
import com.reedelk.openapi.OpenApiSerializableContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.reedelk.openapi.v3.ParameterLocation.query;
import static java.util.Optional.ofNullable;

public class ParameterObject extends OpenApiSerializableAbstract {

    private String name;
    private String description;
    private ParameterLocation in;
    private ParameterStyle style = ParameterStyle.form;
    private Schema schema;
    private String example;
    private Boolean explode;
    private Boolean deprecated;
    private Boolean required;
    private Boolean allowEmptyValue;
    private Boolean allowReserved;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ParameterLocation getIn() {
        return in;
    }

    public void setIn(ParameterLocation in) {
        this.in = in;
    }

    public ParameterStyle getStyle() {
        return style;
    }

    public void setStyle(ParameterStyle style) {
        this.style = style;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema, OpenApiSerializableContext context) {
        context.setSchema(schema);
        this.schema = schema;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Boolean getExplode() {
        return explode;
    }

    public void setExplode(Boolean explode) {
        this.explode = explode;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }

    public Boolean getAllowReserved() {
        return allowReserved;
    }

    public void setAllowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
    }

    @Override
    public Map<String, Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> map = new LinkedHashMap<>();
        set(map, "name", Optional.ofNullable(name).orElse(""));
        set(map, "description", description);
        set(map, "in", ofNullable(in).orElse(query).name().toLowerCase());
        set(map, "style", style.name());
        set(map, "example", example);
        set(map, "explode", explode);
        set(map, "deprecated", deprecated);
        set(map, schema, context);
        // If the parameter location is "path", this property is REQUIRED and its value MUST be true.
        // Otherwise, the property MAY be included and its default value is false.
        if (ParameterLocation.path.equals(in)) {
            set(map, "required", Boolean.TRUE);
        } else {
            set(map, "required", required);
        }
        set(map, "allowEmptyValue", allowEmptyValue);
        set(map, "allowReserved", allowReserved);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {
        name = getString(serialized, "name");
        description = getString(serialized, "description");
        in = ParameterLocation.valueOf(getString(serialized, "in"));
        style = ParameterStyle.valueOf(getString(serialized, "style"));
        // TODO: Schema
        // TODO: Example can it be a reference?
        example = getString(serialized, "example");
        explode = getBoolean(serialized, "explode");
        deprecated = getBoolean(serialized, "deprecated");
        required = getBoolean(serialized, "required");
        allowEmptyValue = getBoolean(serialized, "allowEmptyValue");
        allowReserved = getBoolean(serialized, "allowReserved");
    }
}
