package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import com.reedelk.runtime.api.annotation.Mandatory;
import io.github.classgraph.FieldInfo;

public class MandatoryAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        if (ScannerUtils.hasAnnotation(fieldInfo, Mandatory.class)) {
            builder.mandatory();
        }
    }
}
