package de.codecentric.reedelk.module.descriptor.model.property;

import de.codecentric.reedelk.runtime.api.resource.ResourceText;

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
