package de.codecentric.reedelk.module.descriptor.analyzer.property;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import io.github.classgraph.FieldInfo;

public class NameAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String propertyName = fieldInfo.getName();
        builder.name(propertyName);
    }
}
