package com.reedelk.module.descriptor.model.property;

import com.reedelk.runtime.api.annotation.Password;

public class PasswordDescriptor implements PropertyTypeDescriptor {

    private static final transient Class<?> type = Password.class;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "PasswordDescriptor{" +
                "type=" + type +
                '}';
    }
}
