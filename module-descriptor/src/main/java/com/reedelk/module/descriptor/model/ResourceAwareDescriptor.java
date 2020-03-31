package com.reedelk.module.descriptor.model;

public abstract class ResourceAwareDescriptor implements TypeDescriptor {

    protected String hintBrowseFile;

    public String getHintBrowseFile() {
        return hintBrowseFile;
    }

    public void setHintBrowseFile(String hintBrowseFile) {
        this.hintBrowseFile = hintBrowseFile;
    }
}
