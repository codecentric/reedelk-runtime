package com.reedelk.module.descriptor.model;

public abstract class TabGroupAwareDescriptor implements TypeDescriptor {

    protected String tabGroup;

    public String getTabGroup() {
        return tabGroup;
    }

    public void setTabGroup(String tabGroup) {
        this.tabGroup = tabGroup;
    }
}
