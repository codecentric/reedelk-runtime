package de.codecentric.reedelk.module.descriptor.model.property;

import de.codecentric.reedelk.runtime.api.script.Script;

public class ScriptDescriptor implements PropertyTypeDescriptor {

    private static final transient Class<?> type = Script.class;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ScriptDescriptor{" +
                "type=" + type +
                '}';
    }
}
