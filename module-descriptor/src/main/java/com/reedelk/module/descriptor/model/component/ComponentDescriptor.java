package com.reedelk.module.descriptor.model.component;

import com.reedelk.module.descriptor.model.property.PropertyDescriptor;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComponentDescriptor implements Serializable {

    // Icon and Images are not serialized.
    private transient Icon icon;
    private transient Image image;

    private boolean hidden;
    private String displayName;
    private String description;
    private String fullyQualifiedName;
    private ComponentType type;
    private ComponentInputDescriptor input;
    private ComponentOutputDescriptor output;

    private List<PropertyDescriptor> properties = new ArrayList<>();

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public List<PropertyDescriptor> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyDescriptor> properties) {
        this.properties = properties;
    }

    public Optional<PropertyDescriptor> getPropertyDescriptor(String propertyName) {
        return properties
                .stream()
                .filter(descriptor -> descriptor.getName().equals(propertyName))
                .findFirst();
    }

    public ComponentInputDescriptor getInput() {
        return input;
    }

    public void setInput(ComponentInputDescriptor input) {
        this.input = input;
    }

    public ComponentOutputDescriptor getOutput() {
        return output;
    }

    public void setOutput(ComponentOutputDescriptor output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "ComponentDescriptor{" +
                "hidden=" + hidden +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", fullyQualifiedName='" + fullyQualifiedName + '\'' +
                ", type=" + type +
                ", input=" + input +
                ", output=" + output +
                ", properties=" + properties +
                '}';
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private boolean hidden;
        private String displayName;
        private String description;
        private String fullyQualifiedName;

        private ComponentType type;
        private ComponentInputDescriptor input;
        private ComponentOutputDescriptor output;

        private List<PropertyDescriptor> properties = new ArrayList<>();

        public Builder properties(List<PropertyDescriptor> properties) {
            this.properties.addAll(properties);
            return this;
        }

        public Builder fullyQualifiedName(String fullyQualifiedName) {
            this.fullyQualifiedName = fullyQualifiedName;
            return this;
        }

        public Builder output(ComponentOutputDescriptor output) {
            this.output = output;
            return this;
        }

        public Builder input(ComponentInputDescriptor input) {
            this.input = input;
            return this;
        }

        public Builder type(ComponentType componentType) {
            this.type = componentType;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder hidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        public ComponentDescriptor build() {
            ComponentDescriptor descriptor = new ComponentDescriptor();
            descriptor.hidden = hidden;
            descriptor.displayName = displayName;
            descriptor.description = description;
            descriptor.fullyQualifiedName = fullyQualifiedName;
            descriptor.type = type;
            descriptor.input = input;
            descriptor.output = output;
            descriptor.properties.addAll(properties);
            return descriptor;
        }
    }
}
