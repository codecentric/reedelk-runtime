package com.reedelk.module.descriptor.model;

import java.util.Map;

public class TypeMapDescriptor implements TypeDescriptor {

    private static final transient Class<?> type = Map.class;
    protected String tabGroup;

    @Override
    public Class<?> getType() {
        return type;
    }

    public String getTabGroup() {
        return tabGroup;
    }

    public void setTabGroup(String tabGroup) {
        this.tabGroup = tabGroup;
    }

    @Override
    public String toString() {
        return "TypeMapDescriptor{" +
                "type=" + type +
                ", tabGroup='" + tabGroup + '\'' +
                '}';
    }
}
