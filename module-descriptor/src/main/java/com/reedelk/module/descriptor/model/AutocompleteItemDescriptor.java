package com.reedelk.module.descriptor.model;

import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;

public class AutocompleteItemDescriptor {

    private String type;
    private String token;
    private String example;
    private String returnType;
    private String description;
    private String replaceValue;
    private int cursorOffset;
    private AutocompleteItemType itemType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReplaceValue() {
        return replaceValue;
    }

    public void setReplaceValue(String replaceValue) {
        this.replaceValue = replaceValue;
    }

    public int getCursorOffset() {
        return cursorOffset;
    }

    public void setCursorOffset(int cursorOffset) {
        this.cursorOffset = cursorOffset;
    }

    public AutocompleteItemType getItemType() {
        return itemType;
    }

    public void setItemType(AutocompleteItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "AutocompleteItemDescriptor{" +
                "type='" + type + '\'' +
                ", token='" + token + '\'' +
                ", returnType='" + returnType + '\'' +
                ", description='" + description + '\'' +
                ", replaceValue='" + replaceValue + '\'' +
                ", cursorOffset=" + cursorOffset +
                ", itemType=" + itemType +
                '}';
    }

    public static AutocompleteItemDescriptor.Builder create() {
        return new AutocompleteItemDescriptor.Builder();
    }

    public static class Builder {

        private String type;
        private String token;
        private String example;
        private String returnType;
        private String description;
        private String replaceValue;
        private int cursorOffset;
        private AutocompleteItemType itemType;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder example(String example) {
            this.example = example;
            return this;
        }

        public Builder returnType(String returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder cursorOffset(int cursorOffset) {
            this.cursorOffset = cursorOffset;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder replaceValue(String replaceValue) {
            this.replaceValue = replaceValue;
            return this;
        }

        public Builder itemType(AutocompleteItemType itemType) {
            this.itemType = itemType;
            return this;
        }

        public AutocompleteItemDescriptor build() {
            AutocompleteItemDescriptor descriptor = new AutocompleteItemDescriptor();
            descriptor.type = type;
            descriptor.token = token;
            descriptor.example = example;
            descriptor.itemType = itemType;
            descriptor.returnType = returnType;
            descriptor.description = description;
            descriptor.replaceValue = replaceValue;
            descriptor.cursorOffset = cursorOffset;
            return descriptor;
        }
    }
}

