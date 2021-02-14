package de.codecentric.reedelk.module.descriptor.model.property;

import de.codecentric.reedelk.runtime.api.resource.ResourceBinary;

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
