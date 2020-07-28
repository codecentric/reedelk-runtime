package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableAbstract;
import com.reedelk.openapi.OpenApiSerializableContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class HeaderObject extends OpenApiSerializableAbstract {

    private String description;
    private ParameterStyle style = ParameterStyle.simple;
    private Schema schema;
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
        set(map, schema, context);
        set(map, "example", example);
        set(map, "explode", explode);
        set(map, "deprecated", deprecated);
        set(map, "allowReserved", allowReserved);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {
        description = getString(serialized, "description");
        style = ParameterStyle.valueOf(getString(serialized, "style"));
        // TODO: Deserialize schema
        example = getString(serialized, "example");
        explode = getBoolean(serialized, "explode");
        deprecated = getBoolean(serialized, "deprecated");
        allowReserved = getBoolean(serialized, "allowReserved");
    }
}
