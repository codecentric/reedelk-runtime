package com.reedelk.module.descriptor.model.property;


import java.util.Map;

public class EnumDescriptor implements PropertyTypeDescriptor {

    private static final transient Class<?> type = Enum.class;
    private Map<String, String> nameAndDisplayNameMap;

    @Override
    public Class<?> getType() {
        return type;
    }

    public Map<String, String> getNameAndDisplayNameMap() {
        return nameAndDisplayNameMap;
    }

    public void setNameAndDisplayNameMap(Map<String, String> nameAndDisplayNameMap) {
        this.nameAndDisplayNameMap = nameAndDisplayNameMap;
    }

    @Override
    public String toString() {
        return "EnumDescriptor{" +
                "type=" + type +
                ", nameAndDisplayNameMap=" + nameAndDisplayNameMap +
                '}';
    }
}
