package com.reedelk.module.descriptor.json;

enum PropertyTypeDescriptorAttributes {

    INSTANCE("instance"),
    CLASSNAME("classname"),
    TYPE("type");

    private final String value;

    PropertyTypeDescriptorAttributes(String value) {
        this.value = value;
    }

    String value() {
        return value;
    }
}
