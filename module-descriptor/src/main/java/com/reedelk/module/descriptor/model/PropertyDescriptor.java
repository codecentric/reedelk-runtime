package com.reedelk.module.descriptor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.reedelk.runtime.api.commons.Preconditions.checkState;

public class PropertyDescriptor implements Serializable {

    private String example;
    private String initValue;
    private String hintValue;
    private String displayName;
    private String defaultValue;
    private String propertyName;
    private String propertyDescription;

    private List<WhenDescriptor> whens;

    private TypeDescriptor propertyType;
    private ScriptSignatureDescriptor scriptSignature;
    private AutoCompleteContributorDescriptor autocompleteContributor;

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

    public ScriptSignatureDescriptor getScriptSignature() {
        return scriptSignature;
    }

    public void setScriptSignature(ScriptSignatureDescriptor scriptSignature) {
        this.scriptSignature = scriptSignature;
    }

    public AutoCompleteContributorDescriptor getAutocompleteContributor() {
        return autocompleteContributor;
    }

    public void setAutocompleteContributor(AutoCompleteContributorDescriptor autocompleteContributor) {
        this.autocompleteContributor = autocompleteContributor;
    }

    public List<WhenDescriptor> getWhens() {
        return whens;
    }

    public void setWhens(List<WhenDescriptor> whens) {
        this.whens = whens;
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
                ", scriptSignatureDefinition=" + scriptSignature +
                ", autoCompleteContributorDefinition=" + autocompleteContributor +
                ", whenDefinitions=" + whens +
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
        private ScriptSignatureDescriptor scriptSignature;
        private AutoCompleteContributorDescriptor autocompleteContributor;

        private List<WhenDescriptor> whens = new ArrayList<>();

        public Builder example(String example) {
            this.example = example;
            return this;
        }

        public Builder type(TypeDescriptor type) {
            this.propertyType = type;
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

        public Builder propertyName(String propertyName) {
            this.propertyName = propertyName;
            return this;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder propertyDescription(String propertyDescription) {
            this.propertyDescription = propertyDescription;
            return this;
        }

        public Builder when(WhenDescriptor whenDescriptor) {
            this.whens.add(whenDescriptor);
            return this;
        }

        public Builder scriptSignature(ScriptSignatureDescriptor definition) {
            this.scriptSignature = definition;
            return this;
        }

        public Builder autoCompleteContributor(AutoCompleteContributorDescriptor definition) {
            this.autocompleteContributor = definition;
            return this;
        }

        public PropertyDescriptor build() {
            checkState(propertyName != null, "propertyName");
            checkState(propertyType != null, "propertyType");

            PropertyDescriptor descriptor = new PropertyDescriptor();
            descriptor.example = example;
            descriptor.hintValue = hintValue;
            descriptor.initValue = initValue;
            descriptor.displayName = displayName;
            descriptor.propertyName = propertyName;
            descriptor.propertyType = propertyType;
            descriptor.defaultValue = defaultValue;
            descriptor.whens = whens;
            descriptor.scriptSignature = scriptSignature;
            descriptor.propertyDescription = propertyDescription;
            descriptor.autocompleteContributor = autocompleteContributor;
            return descriptor;
        }
    }
}
