package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import io.github.classgraph.FieldInfo;

public interface DescriptorFactory {

    boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context);

    PropertyTypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context);
}
