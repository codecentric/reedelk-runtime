package com.reedelk.module.descriptor.model;

public class TypeDynamicValueDescriptor implements TypeDescriptor {

    private transient Class<?> type;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public void setType(Class<?> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TypeDynamicValueDescriptor{" +
                "type=" + type +
                '}';
    }
}
