package com.reedelk.module.descriptor.model.property;

public class TypePrimitiveDescriptor implements PropertyTypeDescriptor {

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
        return "TypePrimitiveDescriptor{" +
                "type=" + type +
                '}';
    }
}
