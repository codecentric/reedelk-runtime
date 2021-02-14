package de.codecentric.reedelk.module.descriptor.analyzer.property;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import io.github.classgraph.FieldInfo;

import java.util.Optional;

public class PropertyAnalyzer {

    private final ComponentAnalyzerContext context;

    public PropertyAnalyzer(ComponentAnalyzerContext context) {
        this.context = context;
    }

    public Optional<PropertyDescriptor> analyze(FieldInfo fieldInfo) {
        return ScannerUtils.isVisibleProperty(fieldInfo) ?
                Optional.of(FieldInfoAnalyzers.analyze(fieldInfo, context)) :
                Optional.empty();
    }
}
