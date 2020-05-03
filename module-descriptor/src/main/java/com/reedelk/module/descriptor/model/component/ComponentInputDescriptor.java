package com.reedelk.module.descriptor.model.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComponentInputDescriptor implements Serializable {

    private String description;
    private List<String> payload = new ArrayList<>();

    public List<String> getPayload() {
        return payload;
    }

    public void setPayload(List<String> payload) {
        this.payload = payload;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ComponentInputDescriptor{" +
                "payload=" + payload +
                ", description='" + description + '\'' +
                '}';
    }
}
