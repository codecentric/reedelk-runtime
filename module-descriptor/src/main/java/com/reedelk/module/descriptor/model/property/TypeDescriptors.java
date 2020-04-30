package com.reedelk.module.descriptor.model.property;

public class TypeDescriptors {

    private TypeDescriptors() {
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends PropertyTypeDescriptor> from(String typeDescriptorFullyQualifiedName) {
        try {
            return (Class<? extends PropertyTypeDescriptor>) Class.forName(typeDescriptorFullyQualifiedName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
