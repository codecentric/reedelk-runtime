package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import com.reedelk.runtime.api.annotation.Description;
import io.github.classgraph.FieldInfo;

public class DescriptionAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String propertyDescription =
                ScannerUtils.annotationValueOrDefaultFrom(fieldInfo, Description.class, null);
        builder.description(propertyDescription);
    }
}
