package de.codecentric.reedelk.module.descriptor.analyzer.property;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.WhenDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.When;
import de.codecentric.reedelk.runtime.api.annotation.Whens;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.FieldInfo;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.parameterValueFrom;
import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.repeatableAnnotation;

public class WhenAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        repeatableAnnotation(fieldInfo, When.class, Whens.class).forEach(annotationInfo -> {
            WhenDescriptor whenDescriptor = processWhenInfo(annotationInfo);
            builder.when(whenDescriptor);
        });
    }

    private WhenDescriptor processWhenInfo(AnnotationInfo info) {
        String propertyName = parameterValueFrom(info, "propertyName", StringUtils.EMPTY);
        String propertyValue = parameterValueFrom(info, "propertyValue", StringUtils.EMPTY);
        WhenDescriptor definition = new WhenDescriptor();
        definition.setPropertyName(propertyName);
        definition.setPropertyValue(propertyValue);
        return definition;
    }
}
