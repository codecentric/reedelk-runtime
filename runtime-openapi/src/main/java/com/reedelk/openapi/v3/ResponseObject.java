package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableAbstract;
import com.reedelk.openapi.OpenApiSerializableContext;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseObject extends OpenApiSerializableAbstract {

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

    @SuppressWarnings("unchecked")
    @Override
    public void deserialize(Map<String, Object> serialized) {
        description = getString(serialized, "description");
        if (serialized.containsKey("content")) {
            Map<String,Map<String,Object>> contentMap = (Map<String, Map<String, Object>>) serialized.get("content");
            contentMap.forEach((contentType, mediaTypeObjectMap) -> {
                MediaTypeObject mediaTypeObject = new MediaTypeObject();
                mediaTypeObject.deserialize(mediaTypeObjectMap);
                content.put(contentType, mediaTypeObject);
            });
        }
        if (serialized.containsKey("headers")) {
            Map<String, Map<String,Object>> headersMap = (Map<String, Map<String, Object>>) serialized.get("headers");
            headersMap.forEach((headerName, headerObjectMap) -> {
                HeaderObject headerObject = new HeaderObject();
                headerObject.deserialize(headerObjectMap);
                headers.put(headerName, headerObject);
            });
        }
    }



}
