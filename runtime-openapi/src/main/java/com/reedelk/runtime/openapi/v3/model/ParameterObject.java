package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import com.reedelk.runtime.openapi.v3.PredefinedSchema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.reedelk.runtime.openapi.v3.model.ParameterLocation.query;
import static java.util.Optional.ofNullable;

public class ParameterObject extends AbstractOpenApiSerializable {

    private String name;
    private String description;
    private ParameterLocation in;
    private ParameterStyle style = ParameterStyle.form;
    private PredefinedSchema predefinedSchema = PredefinedSchema.STRING;
    private SchemaReference schema;
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

    public PredefinedSchema getPredefinedSchema() {
        return predefinedSchema;
    }

    public void setPredefinedSchema(PredefinedSchema predefinedSchema) {
        this.predefinedSchema = predefinedSchema;
    }

    public SchemaReference getSchema() {
        return schema;
    }

    public void setSchema(SchemaReference schema) {
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
        map.put("name", Optional.ofNullable(name).orElse(""));
        map.put("description", description);
        map.put("in", ofNullable(in).orElse(query).name().toLowerCase());
        map.put("style", style.name());

        // TODO
       // JsonSchemaUtils.setSchema(context, map, predefinedSchema, schema);

        map.put("example", example);
        map.put("explode", explode);
        map.put("deprecated", deprecated);

        // If the parameter location is "path", this property is REQUIRED and its value MUST be true.
        // Otherwise, the property MAY be included and its default value is false.
        if (ParameterLocation.path.equals(in)) {
            map.put("required", Boolean.TRUE);
        } else {
            map.put("required", required);
        }

        map.put("allowEmptyValue", allowEmptyValue);
        map.put("allowReserved", allowReserved);
        return map;
    }
}
