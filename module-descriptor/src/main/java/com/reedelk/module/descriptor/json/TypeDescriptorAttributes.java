package com.reedelk.module.descriptor.json;

enum TypeDescriptorAttributes {

    INSTANCE("instance"),
    CLASSNAME("classname"),
    TYPE("type");

    private final String value;

    TypeDescriptorAttributes(String value) {
        this.value = value;
    }

    String value() {
        return value;
    }
}
