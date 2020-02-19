package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.ComponentPropertyDescriptor;
import com.reedelk.runtime.api.annotation.Property;
import io.github.classgraph.FieldInfo;

public class PropertyDisplayNameAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, ComponentPropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String displayName = ScannerUtils.annotationValueOrDefaultFrom(fieldInfo, Property.class, fieldInfo.getName());
        if (Property.USE_DEFAULT_NAME.equals(displayName)) {
            String propertyName = fieldInfo.getName();
            builder.displayName(propertyName);
        } else {
            builder.displayName(displayName);
        }
    }
}
