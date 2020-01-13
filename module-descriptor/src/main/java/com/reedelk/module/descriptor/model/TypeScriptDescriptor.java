package com.reedelk.module.descriptor.model;

import com.reedelk.runtime.api.script.Script;

public class TypeScriptDescriptor implements TypeDescriptor {

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
