package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import com.reedelk.module.descriptor.model.WhenDescriptor;
import com.reedelk.runtime.api.annotation.When;
import com.reedelk.runtime.api.annotation.Whens;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.FieldInfo;

public class WhenAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        ScannerUtils.repeatableAnnotation(fieldInfo, When.class, Whens.class).forEach(annotationInfo -> {
            WhenDescriptor whenDescriptor = processWhenInfo(annotationInfo);
            builder.when(whenDescriptor);
        });
    }

    private WhenDescriptor processWhenInfo(AnnotationInfo info) {
        String propertyName = ScannerUtils.stringParameterValueFrom(info, "propertyName");
        String propertyValue = ScannerUtils.stringParameterValueFrom(info, "propertyValue");
        WhenDescriptor definition = new WhenDescriptor();
        definition.setPropertyName(propertyName);
        definition.setPropertyValue(propertyValue);
        return definition;
    }
}
