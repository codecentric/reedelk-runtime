package com.reedelk.module.descriptor.model.property;

import com.reedelk.runtime.api.resource.ResourceText;

public class ResourceTextDescriptor extends ResourceAwareDescriptor {

    private transient Class<?> type = ResourceText.class;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ResourceTextDescriptor{" +
                "widthAuto=" + widthAuto +
                ", hintBrowseFile='" + hintBrowseFile + '\'' +
                '}';
    }
}
