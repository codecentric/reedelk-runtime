package com.reedelk.module.descriptor.model;

import com.reedelk.runtime.api.resource.ResourceBinary;

public class TypeResourceBinaryDescriptor extends ResourceAwareDescriptor {

    private transient Class<?> type = ResourceBinary.class;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TypeResourceBinaryDescriptor{" +
                "type=" + type +
                ", hintBrowseFile='" + hintBrowseFile + '\'' +
                '}';
    }
}
