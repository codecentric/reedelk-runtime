package com.reedelk.module.descriptor.model;


import java.util.Map;

public class TypeEnumDescriptor implements TypeDescriptor {

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
        return "TypeEnumDescriptor{" +
                "type=" + type +
                ", nameAndDisplayNameMap=" + nameAndDisplayNameMap +
                '}';
    }
}
