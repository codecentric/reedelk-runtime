package com.reedelk.module.descriptor.model;

import com.reedelk.runtime.api.resource.ResourceBinary;

public class TypeResourceBinaryDescriptor implements TypeDescriptor {

    private static final transient Class<?> type = ResourceBinary.class;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TypeResourceBinaryDescriptor{" +
                "type=" + type +
                '}';
    }
}
