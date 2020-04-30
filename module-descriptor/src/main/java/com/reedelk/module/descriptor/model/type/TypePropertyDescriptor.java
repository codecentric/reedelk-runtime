package com.reedelk.module.descriptor.model.type;

public class TypePropertyDescriptor {

    private String name;
    private String type; // The property type.
    private String example;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TypePropertyDescriptor{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", example='" + example + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
