package com.reedelk.module.descriptor.model.property;

import com.reedelk.runtime.api.script.Script;

public class TypeScriptDescriptor implements PropertyTypeDescriptor {

    private static final transient Class<?> type = Script.class;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TypeScriptDescriptor{" +
                "type=" + type +
                '}';
    }
}
