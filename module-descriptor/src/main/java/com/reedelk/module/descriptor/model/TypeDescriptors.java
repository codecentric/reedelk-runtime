package com.reedelk.module.descriptor.model;

public class TypeDescriptors {

    private TypeDescriptors() {
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends TypeDescriptor> from(String typeDescriptorFullyQualifiedName) {
        try {
            return (Class<? extends TypeDescriptor>) Class.forName(typeDescriptorFullyQualifiedName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
