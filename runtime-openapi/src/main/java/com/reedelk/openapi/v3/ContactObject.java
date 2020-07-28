package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableAbstract;
import com.reedelk.openapi.OpenApiSerializableContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class ContactObject extends OpenApiSerializableAbstract {

    private String name;
    private String url;
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Map<String,Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> map = new LinkedHashMap<>();
        set(map, "name", name);
        set(map, "url", url);
        set(map, "email", email);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {
        name = getString(serialized, "name");
        url = getString (serialized, "url");
        email = getString(serialized, "email");
    }
}
