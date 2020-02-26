package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import io.github.classgraph.FieldInfo;

public class NameAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String propertyName = fieldInfo.getName();
        builder.name(propertyName);
    }
}
