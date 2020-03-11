package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.module.descriptor.model.TypeResourceBinaryDescriptor;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.isResourceBinary;

public class TypeResourceBinaryFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(Class<?> clazz, FieldInfo fieldInfo) {
        return isResourceBinary(clazz);
    }

    @Override
    public TypeDescriptor create(Class<?> clazz, FieldInfo fieldInfo) {
        return new TypeResourceBinaryDescriptor();
    }
}
