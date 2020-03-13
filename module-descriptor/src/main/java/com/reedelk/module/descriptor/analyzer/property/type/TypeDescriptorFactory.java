package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.TypeDescriptor;
import io.github.classgraph.FieldInfo;

public interface TypeDescriptorFactory {

    boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context);

    TypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context);
}
