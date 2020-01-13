package com.reedelk.module.descriptor.model;

import com.reedelk.runtime.api.resource.ResourceText;

public class TypeResourceTextDescriptor implements TypeDescriptor {

    private static final transient Class<?> type = ResourceText.class;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TypeResourceTextDescriptor{" +
                "type=" + type +
                '}';
    }
}