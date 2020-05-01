package com.reedelk.module.descriptor.model.property;

import com.reedelk.module.descriptor.model.commons.WhenDescriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.reedelk.runtime.api.commons.Preconditions.checkState;

public class PropertyDescriptor implements Serializable {

    private String name;
    private String group;
    private String example;
    private String initValue;
    private String hintValue;
    private String description;
    private String displayName;
    private String defaultValue;
    private PropertyTypeDescriptor type;
    private ScriptSignatureDescriptor scriptSignature;

    private List<WhenDescriptor> whens;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getInitValue() {
        return initValue;
    }

    public void setInitValue(String initValue) {
        this.initValue = initValue;
    }

    public String getHintValue() {
        return hintValue;
    }

    public void setHintValue(String hintValue) {
        this.hintValue = hintValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @SuppressWarnings("unchecked")
    public <T extends PropertyTypeDescriptor> T getType() {
        return (T) type;
    }

    public void setType(PropertyTypeDescriptor type) {
        this.type = type;
    }

    public ScriptSignatureDescriptor getScriptSignature() {
        return scriptSignature;
    }

    public void setScriptSignature(ScriptSignatureDescriptor scriptSignature) {
        this.scriptSignature = scriptSignature;
    }

    public List<WhenDescriptor> getWhens() {
        return whens;
    }

    public void setWhens(List<WhenDescriptor> whens) {
        this.whens = whens;
    }

    @Override
    public String toString() {
        return "PropertyDescriptor{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", example='" + example + '\'' +
                ", initValue='" + initValue + '\'' +
                ", hintValue='" + hintValue + '\'' +
                ", description='" + description + '\'' +
                ", displayName='" + displayName + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", type=" + type +
                ", scriptSignature=" + scriptSignature +
                ", whens=" + whens +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String group;
        private String example;
        private String hintValue;
        private String initValue;
        private String description;
        private String displayName;
        private String defaultValue;
        private PropertyTypeDescriptor type;
        private ScriptSignatureDescriptor scriptSignature;

        private List<WhenDescriptor> whens = new ArrayList<>();

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder example(String example) {
            this.example = example;
            return this;
        }

        public Builder name(String propertyName) {
            this.name = propertyName;
            return this;
        }

        public Builder initValue(String initValue) {
            this.initValue = initValue;
            return this;
        }

        public Builder hintValue(String hintValue) {
            this.hintValue = hintValue;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder type(PropertyTypeDescriptor type) {
            this.type = type;
            return this;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder when(WhenDescriptor whenDescriptor) {
            this.whens.add(whenDescriptor);
            return this;
        }

        public Builder description(String propertyDescription) {
            this.description = propertyDescription;
            return this;
        }

        public Builder scriptSignature(ScriptSignatureDescriptor definition) {
            this.scriptSignature = definition;
            return this;
        }

        public PropertyDescriptor build() {
            checkState(name != null, "propertyName");
            checkState(type != null, "propertyType");

            PropertyDescriptor descriptor = new PropertyDescriptor();
            descriptor.name = name;
            descriptor.type = type;
            descriptor.group = group;
            descriptor.whens = whens;
            descriptor.example = example;
            descriptor.hintValue = hintValue;
            descriptor.initValue = initValue;
            descriptor.description = description;
            descriptor.displayName = displayName;
            descriptor.defaultValue = defaultValue;
            descriptor.scriptSignature = scriptSignature;
            return descriptor;
        }
    }
}
