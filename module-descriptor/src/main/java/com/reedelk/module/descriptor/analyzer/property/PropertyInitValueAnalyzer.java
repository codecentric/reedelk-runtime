package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import com.reedelk.runtime.api.annotation.InitValue;
import io.github.classgraph.FieldInfo;

public class PropertyInitValueAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String initValueAsString =
                ScannerUtils.annotationValueOrDefaultFrom(fieldInfo, InitValue.class, InitValue.USE_DEFAULT_VALUE);
        builder.initValue(initValueAsString);
    }
}
