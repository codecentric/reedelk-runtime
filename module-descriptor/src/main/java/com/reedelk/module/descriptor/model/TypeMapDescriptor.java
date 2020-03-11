package com.reedelk.module.descriptor.model;

import java.util.Map;

public class TypeMapDescriptor implements TypeDescriptor {

    private static final transient Class<?> type = Map.class;

    protected String tabGroup;
    protected TabPlacement tabPlacement;
    protected String valueFullyQualifiedName;

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

    public TabPlacement getTabPlacement() {
        return tabPlacement;
    }

    public void setTabPlacement(TabPlacement tabPlacement) {
        this.tabPlacement = tabPlacement;
    }

    public String getValueFullyQualifiedName() {
        return valueFullyQualifiedName;
    }

    public void setValueFullyQualifiedName(String valueFullyQualifiedName) {
        this.valueFullyQualifiedName = valueFullyQualifiedName;
    }

    @Override
    public String toString() {
        return "TypeMapDescriptor{" +
                "type=" + type +
                ", tabGroup='" + tabGroup + '\'' +
                '}';
    }
}
