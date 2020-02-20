package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import com.reedelk.runtime.api.annotation.Example;
import io.github.classgraph.FieldInfo;

public class PropertyExampleAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String example = ScannerUtils.annotationValueOrDefaultFrom(fieldInfo, Example.class, null);
        builder.example(example);
    }
}
