package de.codecentric.reedelk.module.descriptor.analyzer.property;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.DefaultValue;
import io.github.classgraph.FieldInfo;

public class DefaultValueAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String defaultValue = ScannerUtils.annotationValueFrom(fieldInfo, DefaultValue.class, null);
        builder.defaultValue(defaultValue);
    }
}
