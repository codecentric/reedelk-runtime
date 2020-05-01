package com.reedelk.module.descriptor.model.component;

import java.util.List;

public class ComponentOutputDescriptor {

    private String attributes;
    private String description;
    private List<String> payload;

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPayload() {
        return payload;
    }

    public void setPayload(List<String> payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "ComponentOutputDescriptor{" +
                "attributes='" + attributes + '\'' +
                ", description='" + description + '\'' +
                ", payload=" + payload +
                '}';
    }
}
