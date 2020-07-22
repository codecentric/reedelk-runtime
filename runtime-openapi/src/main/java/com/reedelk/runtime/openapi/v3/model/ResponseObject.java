package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseObject extends AbstractOpenApiSerializable {

    private String description;
    private Map<String, MediaTypeObject> content = new HashMap<>();
    private Map<String, HeaderObject> headers = new HashMap<>();

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

    public Map<String, HeaderObject> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, HeaderObject> headers) {
        this.headers = headers;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> responseObject = new LinkedHashMap<>();
        set(responseObject, "description", description);
        set(responseObject, "content", content, context);
        set(responseObject, "headers", headers, context);
        return responseObject;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {

    }
}
