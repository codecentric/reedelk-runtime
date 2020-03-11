package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.model.TypeDescriptor;
import io.github.classgraph.FieldInfo;

public interface TypeDescriptorFactory {

    boolean test(Class<?> clazz, FieldInfo fieldInfo);

    TypeDescriptor create(Class<?> clazz, FieldInfo fieldInfo);
}
