package com.reedelk.module.descriptor.model;

import java.util.Map;

public class TypeMapDescriptor extends TabGroupAwareDescriptor {

    private static final transient Class<?> TYPE = Map.class;

    protected String keyName;
    protected String valueName;
    protected TypeDescriptor valueType;

    @Override
    public Class<?> getType() {
        return TYPE;
    }

    public TypeDescriptor getValueType() {
        return valueType;
    }

    public void setValueType(TypeDescriptor valueType) {
        this.valueType = valueType;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    @Override
    public String toString() {
        return "TypeMapDescriptor{" +
                "tabGroup='" + tabGroup + '\'' +
                ", keyName='" + keyName + '\'' +
                ", valueName='" + valueName + '\'' +
                ", valueType=" + valueType +
                '}';
    }
}
