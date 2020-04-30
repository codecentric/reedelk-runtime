package com.reedelk.module.descriptor.model.property;

import com.reedelk.runtime.api.resource.ResourceText;

public class TypeResourceTextDescriptor extends ResourceAwareDescriptor {

    private transient Class<?> type = ResourceText.class;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TypeResourceTextDescriptor{" +
                "widthAuto=" + widthAuto +
                ", hintBrowseFile='" + hintBrowseFile + '\'' +
                '}';
    }
}
