package de.codecentric.reedelk.module.descriptor.analyzer.property;

import de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.InitValue;
import io.github.classgraph.FieldInfo;

public class InitValueAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        String initValueAsString =
                ScannerUtils.annotationValueFrom(fieldInfo, InitValue.class, InitValue.USE_DEFAULT_VALUE);
        builder.initValue(initValueAsString);
    }
}
