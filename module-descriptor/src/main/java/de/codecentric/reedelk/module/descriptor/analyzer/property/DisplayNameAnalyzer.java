package de.codecentric.reedelk.module.descriptor.analyzer.property;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.Property;
import io.github.classgraph.FieldInfo;

public class DisplayNameAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String displayName = ScannerUtils.annotationValueFrom(fieldInfo, Property.class, fieldInfo.getName());
        if (Property.USE_DEFAULT_NAME.equals(displayName)) {
            String propertyName = fieldInfo.getName();
            builder.displayName(propertyName);
        } else {
            builder.displayName(displayName);
        }
    }
}
