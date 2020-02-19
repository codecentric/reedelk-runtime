package com.reedelk.module.descriptor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.reedelk.runtime.api.commons.Preconditions.checkState;

public class ComponentPropertyDescriptor implements Serializable {

    private String example;
    private String initValue;
    private String hintValue;
    private String displayName;
    private String defaultValue;
    private String propertyName;
    private String propertyDescription;
    private TypeDescriptor propertyType;
    private List<WhenDescriptor> whenDescriptors;
    private ScriptSignatureDescriptor scriptSignatureDescriptor;
    private AutoCompleteContributorDescriptor autoCompleteContributorDescriptor;

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getHintValue() {
        return hintValue;
    }

    public void setHintValue(String hintValue) {
        this.hintValue = hintValue;
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

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setInitValue(String initValue) {
        this.initValue = initValue;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    @SuppressWarnings("unchecked")
    public <T extends TypeDescriptor> T getPropertyType() {
        return (T) propertyType;
    }

    public void setPropertyType(TypeDescriptor propertyType) {
        this.propertyType = propertyType;
    }

    public ScriptSignatureDescriptor getScriptSignatureDescriptor() {
        return scriptSignatureDescriptor;
    }

    public void setScriptSignatureDescriptor(ScriptSignatureDescriptor scriptSignatureDescriptor) {
        this.scriptSignatureDescriptor = scriptSignatureDescriptor;
    }

    public AutoCompleteContributorDescriptor getAutoCompleteContributorDescriptor() {
        return autoCompleteContributorDescriptor;
    }

    public void setAutoCompleteContributorDescriptor(AutoCompleteContributorDescriptor autoCompleteContributorDescriptor) {
        this.autoCompleteContributorDescriptor = autoCompleteContributorDescriptor;
    }

    public List<WhenDescriptor> getWhenDescriptors() {
        return whenDescriptors;
    }

    public void setWhenDescriptors(List<WhenDescriptor> whenDescriptors) {
        this.whenDescriptors = whenDescriptors;
    }

    public String getInitValue() {
        return initValue;
    }

    @Override
    public String toString() {
        return "ComponentPropertyDescriptor{" +
                "example='" + example + '\'' +
                ", hintValue='" + hintValue + '\'' +
                ", displayName='" + displayName + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", initValue='" + initValue + '\'' +
                ", propertyDescription='" + propertyDescription + '\'' +
                ", propertyType=" + propertyType +
                ", scriptSignatureDefinition=" + scriptSignatureDescriptor +
                ", autoCompleteContributorDefinition=" + autoCompleteContributorDescriptor +
                ", whenDefinitions=" + whenDescriptors +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String example;
        private String hintValue;
        private String initValue;
        private String displayName;
        private String propertyName;
        private String defaultValue;
        private String propertyDescription;
        private TypeDescriptor propertyType;
        private ScriptSignatureDescriptor scriptSignatureDescriptor;
        private AutoCompleteContributorDescriptor autoCompleteContributionsDefinition;

        private List<WhenDescriptor> whenDescriptors = new ArrayList<>();

        public Builder example(String example) {
            this.example = example;
            return this;
        }

        public Builder type(TypeDescriptor type) {
            this.propertyType = type;
            return this;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
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

        public Builder propertyName(String propertyName) {
            this.propertyName = propertyName;
            return this;
        }

        public Builder initValue(String initValue) {
            this.initValue = initValue;
            return this;
        }

        public Builder propertyDescription(String propertyDescription) {
            this.propertyDescription = propertyDescription;
            return this;
        }

        public Builder when(WhenDescriptor whenDescriptor) {
            this.whenDescriptors.add(whenDescriptor);
            return this;
        }

        public Builder scriptSignature(ScriptSignatureDescriptor definition) {
            this.scriptSignatureDescriptor = definition;
            return this;
        }

        public Builder autoCompleteContributor(AutoCompleteContributorDescriptor definition) {
            this.autoCompleteContributionsDefinition = definition;
            return this;
        }

        public ComponentPropertyDescriptor build() {
            checkState(propertyName != null, "propertyName");
            checkState(propertyType != null, "propertyType");

            ComponentPropertyDescriptor descriptor = new ComponentPropertyDescriptor();
            descriptor.example = example;
            descriptor.hintValue = hintValue;
            descriptor.initValue = initValue;
            descriptor.displayName = displayName;
            descriptor.propertyName = propertyName;
            descriptor.propertyType = propertyType;
            descriptor.defaultValue = defaultValue;
            descriptor.propertyDescription = propertyDescription;
            descriptor.scriptSignatureDescriptor = scriptSignatureDescriptor;
            descriptor.autoCompleteContributorDescriptor = autoCompleteContributionsDefinition;
            descriptor.whenDescriptors = whenDescriptors;
            return descriptor;
        }
    }
}
