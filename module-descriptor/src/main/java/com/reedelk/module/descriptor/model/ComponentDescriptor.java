package com.reedelk.module.descriptor.model;

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
    private ComponentType componentType;
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

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
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
                .filter(descriptor -> descriptor.getPropertyName().equals(propertyName))
                .findFirst();
    }

    @Override
    public String toString() {
        return "ComponentDescriptor{" +
                "icon=" + icon +
                ", image=" + image +
                ", hidden=" + hidden +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", fullyQualifiedName='" + fullyQualifiedName + '\'' +
                ", componentType=" + componentType +
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
        private ComponentType componentType;
        private List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();

        public Builder propertyDescriptors(List<PropertyDescriptor> propertyDescriptors) {
            this.propertyDescriptors.addAll(propertyDescriptors);
            return this;
        }

        public Builder fullyQualifiedName(String fullyQualifiedName) {
            this.fullyQualifiedName = fullyQualifiedName;
            return this;
        }

        public Builder componentType(ComponentType componentType) {
            this.componentType = componentType;
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
            descriptor.componentType = componentType;
            descriptor.fullyQualifiedName = fullyQualifiedName;
            descriptor.properties.addAll(propertyDescriptors);
            return descriptor;
        }
    }
}
