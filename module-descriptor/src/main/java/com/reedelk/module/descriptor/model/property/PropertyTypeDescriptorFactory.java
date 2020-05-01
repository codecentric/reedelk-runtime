package com.reedelk.module.descriptor.model.property;

public class PropertyTypeDescriptorFactory {

    private PropertyTypeDescriptorFactory() {
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends PropertyTypeDescriptor> from(String propertyTypeDescriptorFullyQualifiedName) {
        try {
            return (Class<? extends PropertyTypeDescriptor>) Class.forName(propertyTypeDescriptorFullyQualifiedName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
