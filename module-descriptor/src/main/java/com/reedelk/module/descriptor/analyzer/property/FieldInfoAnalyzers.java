package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.ComponentPropertyDescriptor;
import io.github.classgraph.FieldInfo;

import java.util.Arrays;
import java.util.List;

public class FieldInfoAnalyzers {

    private FieldInfoAnalyzers() {
    }

    private static final List<FieldInfoAnalyzer> FIELD_INFO_ANALYZERS = Arrays.asList(
            new PropertyNameFieldInfoAnalyzer(),
            new PropertyInfoFieldInfoAnalyzer(),
            new TypeFieldInfoAnalyzer(),
            new WhenFieldInfoAnalyzer(),
            new HintFieldInfoAnalyzer(),
            new DisplayNameFieldInfoAnalyzer(),
            new DefaultValueFieldInfoAnalyzer(),
            new ScriptSignatureFieldInfoAnalyzer(),
            new AutoCompleteContributorFieldInfoAnalyzer());

    public static ComponentPropertyDescriptor descriptor(FieldInfo propertyInfo, ComponentAnalyzerContext context) {
        ComponentPropertyDescriptor.Builder builder = ComponentPropertyDescriptor.builder();
        FIELD_INFO_ANALYZERS.forEach(handler -> handler.handle(propertyInfo, builder, context));
        return builder.build();
    }
}
