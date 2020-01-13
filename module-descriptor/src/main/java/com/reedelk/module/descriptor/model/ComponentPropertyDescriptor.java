package com.reedelk.module.descriptor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.reedelk.runtime.api.commons.Preconditions.checkState;

public class ComponentPropertyDescriptor implements Serializable {

    private String hintValue;
    private String displayName;
    private String propertyName;
    private String defaultValue;
    private String propertyInfo;
    private TypeDescriptor propertyType;
    private List<WhenDescriptor> whenDescriptors;
    private ScriptSignatureDescriptor scriptSignatureDescriptor;
    private AutoCompleteContributorDescriptor autoCompleteContributorDescriptor;

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

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getPropertyInfo() {
        return propertyInfo;
    }

    public void setPropertyInfo(String propertyInfo) {
        this.propertyInfo = propertyInfo;
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

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return "ComponentPropertyDescriptor{" +
                "hintValue='" + hintValue + '\'' +
                ", displayName='" + displayName + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", propertyInfo='" + propertyInfo + '\'' +
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

        private String hintValue;
        private String displayName;
        private String propertyName;
        private String defaultValue;
        private String propertyInfo;
        private TypeDescriptor propertyType;
        private ScriptSignatureDescriptor scriptSignatureDescriptor;
        private AutoCompleteContributorDescriptor autoCompleteContributionsDefinition;

        private List<WhenDescriptor> whenDescriptors = new ArrayList<>();

        public Builder type(TypeDescriptor type) {
            this.propertyType = type;
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

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder propertyInfo(String propertyInfo) {
            this.propertyInfo = propertyInfo;
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
            descriptor.hintValue = hintValue;
            descriptor.displayName = displayName;
            descriptor.propertyName = propertyName;
            descriptor.defaultValue = defaultValue;
            descriptor.propertyType = propertyType;
            descriptor.propertyInfo = propertyInfo;
            descriptor.scriptSignatureDescriptor = scriptSignatureDescriptor;
            descriptor.autoCompleteContributorDescriptor = autoCompleteContributionsDefinition;
            descriptor.whenDescriptors = whenDescriptors;
            return descriptor;
        }
    }
}