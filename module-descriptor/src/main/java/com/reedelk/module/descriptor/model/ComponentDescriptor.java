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
    private String fullyQualifiedName;
    private ComponentType componentType;
    private List<ComponentPropertyDescriptor> componentPropertyDescriptors = new ArrayList<>();

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

    public List<ComponentPropertyDescriptor> getComponentPropertyDescriptors() {
        return componentPropertyDescriptors;
    }

    public void setComponentPropertyDescriptors(List<ComponentPropertyDescriptor> componentPropertyDescriptors) {
        this.componentPropertyDescriptors = componentPropertyDescriptors;
    }

    public Optional<ComponentPropertyDescriptor> getPropertyDescriptor(String propertyName) {
        return componentPropertyDescriptors
                .stream()
                .filter(descriptor -> descriptor.getPropertyName().equals(propertyName))
                .findFirst();
    }

    @Override
    public String toString() {
        return "ComponentDefaultDescriptor{" +
                "hidden=" + hidden +
                ", icon=" + icon +
                ", image=" + image +
                ", displayName='" + displayName + '\'' +
                ", fullyQualifiedName='" + fullyQualifiedName + '\'' +
                ", componentType=" + componentType +
                ", componentPropertyDescriptors=" + componentPropertyDescriptors +
                '}';
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private boolean hidden;
        private String displayName;
        private String fullyQualifiedName;
        private ComponentType componentType;
        private List<ComponentPropertyDescriptor> componentPropertyDescriptors = new ArrayList<>();

        public Builder propertyDescriptors(List<ComponentPropertyDescriptor> componentPropertyDescriptors) {
            this.componentPropertyDescriptors.addAll(componentPropertyDescriptors);
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

        public Builder hidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        public ComponentDescriptor build() {
            ComponentDescriptor descriptor = new ComponentDescriptor();
            descriptor.hidden = hidden;
            descriptor.displayName = displayName;
            descriptor.componentType = componentType;
            descriptor.fullyQualifiedName = fullyQualifiedName;
            descriptor.componentPropertyDescriptors.addAll(componentPropertyDescriptors);
            return descriptor;
        }
    }
}