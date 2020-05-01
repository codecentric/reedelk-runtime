package com.reedelk.module.descriptor.model.property;

import com.reedelk.runtime.api.resource.ResourceBinary;

public class ResourceBinaryDescriptor extends ResourceAwareDescriptor {

    private transient Class<?> type = ResourceBinary.class;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ResourceBinaryDescriptor{" +
                "widthAuto=" + widthAuto +
                ", hintBrowseFile='" + hintBrowseFile + '\'' +
                '}';
    }
}
