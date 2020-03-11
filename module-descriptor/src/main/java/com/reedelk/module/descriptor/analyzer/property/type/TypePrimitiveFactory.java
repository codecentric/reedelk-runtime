package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.module.descriptor.model.TypePrimitiveDescriptor;
import io.github.classgraph.FieldInfo;

public class TypePrimitiveFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(Class<?> clazz, FieldInfo fieldInfo) {
        return true;
    }

    @Override
    public TypeDescriptor create(Class<?> clazz, FieldInfo fieldInfo) {
        TypePrimitiveDescriptor descriptor = new TypePrimitiveDescriptor();
        descriptor.setType(clazz);
        return descriptor;
    }
}
