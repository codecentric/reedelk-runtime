package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.JsonSchemaUtils;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import com.reedelk.runtime.openapi.v3.PredefinedSchema;

import java.util.LinkedHashMap;
import java.util.Map;

public class HeaderObject extends AbstractOpenApiSerializable {

    private String description;
    private ParameterStyle style = ParameterStyle.simple;
    private PredefinedSchema predefinedSchema = PredefinedSchema.STRING;
    private SchemaReference schema;
    private String example;

    private Boolean explode;
    private Boolean deprecated;
    private Boolean allowReserved;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getAllowReserved() {
        return allowReserved;
    }

    public void setAllowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> map = new LinkedHashMap<>();
        set(map, "description", description);
        set(map, "style", style.name());

        JsonSchemaUtils.setSchema(context, map, predefinedSchema, schema);

        set(map, "example", example);
        set(map, "explode", explode);
        set(map, "deprecated", deprecated);
        set(map, "allowReserved", allowReserved);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {

    }
}
