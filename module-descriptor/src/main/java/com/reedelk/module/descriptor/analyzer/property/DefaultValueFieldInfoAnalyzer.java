package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.ComponentPropertyDescriptor;
import com.reedelk.runtime.api.annotation.Default;
import io.github.classgraph.FieldInfo;

public class DefaultValueFieldInfoAnalyzer implements FieldInfoAnalyzer {
    @Override
    public void handle(FieldInfo propertyInfo, ComponentPropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String stringValue = ScannerUtils.annotationValueOrDefaultFrom(propertyInfo, Default.class, Default.USE_DEFAULT_VALUE);
        builder.defaultValue(stringValue);
    }
}
