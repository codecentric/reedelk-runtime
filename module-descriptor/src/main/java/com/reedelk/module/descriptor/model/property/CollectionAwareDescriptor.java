package com.reedelk.module.descriptor.model.property;

public abstract class CollectionAwareDescriptor implements PropertyTypeDescriptor {

    protected String tabGroup;
    protected String dialogTitle;

    public String getTabGroup() {
        return tabGroup;
    }

    public void setTabGroup(String tabGroup) {
        this.tabGroup = tabGroup;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }
}
