package com.reedelk.module.descriptor.model;

import java.util.List;

public class TypeListDescriptor implements TypeDescriptor {

    private static final transient Class<?> TYPE = List.class;
    private TypeDescriptor valueType;

    @Override
    public Class<?> getType() {
        return TYPE;
    }

    public void setValueType(TypeDescriptor valueType) {
        this.valueType = valueType;
    }

    public TypeDescriptor getValueType() {
        return valueType;
    }

    @Override
    public String toString() {
        return "TypeListDescriptor{" +
                "valueType=" + valueType +
                '}';
    }
}
