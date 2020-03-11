package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.module.descriptor.model.TypeDynamicValueDescriptor;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.isDynamicValue;

public class TypeDynamicFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(Class<?> clazz, FieldInfo fieldInfo) {
        return isDynamicValue(clazz);
    }

    @Override
    public TypeDescriptor create(Class<?> clazz, FieldInfo fieldInfo) {
        TypeDynamicValueDescriptor descriptor = new TypeDynamicValueDescriptor();
        descriptor.setType(clazz);
        return descriptor;
    }
}
