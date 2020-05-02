package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import com.reedelk.module.descriptor.model.property.WhenDescriptor;
import com.reedelk.runtime.api.annotation.When;
import com.reedelk.runtime.api.annotation.Whens;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.parameterValueFrom;
import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.repeatableAnnotation;
import static com.reedelk.runtime.api.commons.StringUtils.EMPTY;

public class WhenAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        repeatableAnnotation(fieldInfo, When.class, Whens.class).forEach(annotationInfo -> {
            WhenDescriptor whenDescriptor = processWhenInfo(annotationInfo);
            builder.when(whenDescriptor);
        });
    }

    private WhenDescriptor processWhenInfo(AnnotationInfo info) {
        String propertyName = parameterValueFrom(info, "propertyName", EMPTY);
        String propertyValue = parameterValueFrom(info, "propertyValue", EMPTY);
        WhenDescriptor definition = new WhenDescriptor();
        definition.setPropertyName(propertyName);
        definition.setPropertyValue(propertyValue);
        return definition;
    }
}
