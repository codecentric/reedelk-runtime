package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.OpenApiSerializableAbstract;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class LicenseObject extends OpenApiSerializableAbstract {

    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Map<String, Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> map = new LinkedHashMap<>();
        set(map, "name", ofNullable(name).orElse("API License"));
        set(map, "url", url);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {
        name = getString(serialized, "name");
        url = getString(serialized, "url");
    }
}
