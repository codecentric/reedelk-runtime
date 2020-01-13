package com.reedelk.module.descriptor.model;

import com.reedelk.runtime.api.annotation.Password;

public class TypePasswordDescriptor implements TypeDescriptor {

    private static final transient Class<?> type = Password.class;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TypePasswordDescriptor{" +
                "type=" + type +
                '}';
    }
}
