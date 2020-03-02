package com.reedelk.module.descriptor.model;

public class AutocompleteTypeDescriptor {

    private boolean global;
    private String type;
    private String description;

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AutocompleteTypeDescriptor{" +
                "global=" + global +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private boolean global;
        private String type;
        private String description;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder global(boolean global) {
            this.global = global;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public AutocompleteTypeDescriptor build() {
            AutocompleteTypeDescriptor descriptor = new AutocompleteTypeDescriptor();
            descriptor.type = type;
            descriptor.global = global;
            descriptor.description = description;
            return descriptor;
        }
    }
}
