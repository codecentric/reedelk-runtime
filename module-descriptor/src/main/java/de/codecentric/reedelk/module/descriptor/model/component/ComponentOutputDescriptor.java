package de.codecentric.reedelk.module.descriptor.model.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComponentOutputDescriptor implements Serializable {

    private String description;
    private String dynamicPropertyName;
    private List<String> payload = new ArrayList<>();
    private List<String> attributes = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDynamicPropertyName() {
        return dynamicPropertyName;
    }

    public void setDynamicPropertyName(String dynamicPropertyName) {
        this.dynamicPropertyName = dynamicPropertyName;
    }

    public List<String> getPayload() {
        return payload;
    }

    public void setPayload(List<String> payload) {
        this.payload = payload;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "ComponentOutputDescriptor{" +
                "description='" + description + '\'' +
                ", dynamicPropertyName='" + dynamicPropertyName + '\'' +
                ", payload=" + payload +
                ", attributes=" + attributes +
                '}';
    }
}
