package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import com.reedelk.runtime.api.annotation.Group;
import io.github.classgraph.FieldInfo;

public class GroupAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String group = ScannerUtils.annotationValueOrDefaultFrom(fieldInfo, Group.class, null);
        builder.group(group);
    }
}
