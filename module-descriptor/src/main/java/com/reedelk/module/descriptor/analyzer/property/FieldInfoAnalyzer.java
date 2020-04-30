package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import io.github.classgraph.FieldInfo;

public interface FieldInfoAnalyzer {

    void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context);
}
