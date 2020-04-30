package com.reedelk.module.descriptor.model.property;

public abstract class ResourceAwareDescriptor implements PropertyTypeDescriptor {

    protected Boolean widthAuto;
    protected String hintBrowseFile;

    public String getHintBrowseFile() {
        return hintBrowseFile;
    }

    public void setHintBrowseFile(String hintBrowseFile) {
        this.hintBrowseFile = hintBrowseFile;
    }

    public Boolean getWidthAuto() {
        return widthAuto;
    }

    public void setWidthAuto(Boolean widthAuto) {
        this.widthAuto = widthAuto;
    }
}
