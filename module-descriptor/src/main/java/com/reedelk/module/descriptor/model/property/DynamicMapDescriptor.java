package com.reedelk.module.descriptor.model.property;

public class DynamicMapDescriptor extends MapDescriptor {

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
        return "DynamicMapDescriptor{" +
                "type=" + type +
                ", tabGroup='" + tabGroup + '\'' +
                '}';
    }
}
