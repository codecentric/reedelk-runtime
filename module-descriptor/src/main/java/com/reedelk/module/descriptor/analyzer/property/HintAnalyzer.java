package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import com.reedelk.runtime.api.annotation.Hint;
import io.github.classgraph.FieldInfo;

public class HintAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String hintValue = ScannerUtils.annotationValueOrDefaultFrom(fieldInfo, Hint.class, null);
        builder.hintValue(hintValue);
    }
}
