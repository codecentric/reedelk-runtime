package de.codecentric.reedelk.module.descriptor.analyzer.component;

public class UnsupportedType extends RuntimeException {

    public UnsupportedType(String unsupportedTypeQualifiedName) {
        super(String.format("PropertyTypeDescriptor could not be found for type %s.", unsupportedTypeQualifiedName));
    }

    public UnsupportedType(Class<?> unsupportedTypeClazz) {
        this(unsupportedTypeClazz.getName());
    }
}
