package de.codecentric.reedelk.module.descriptor.analyzer.property;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.Description;
import io.github.classgraph.FieldInfo;

public class DescriptionAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String propertyDescription =
                ScannerUtils.annotationValueFrom(fieldInfo, Description.class, null);
        builder.description(propertyDescription);
    }
}
