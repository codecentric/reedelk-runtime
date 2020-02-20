package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import io.github.classgraph.FieldInfo;

import java.util.Arrays;
import java.util.List;

public class FieldInfoAnalyzers {

    private FieldInfoAnalyzers() {
    }

    private static final List<FieldInfoAnalyzer> FIELD_INFO_ANALYZERS = Arrays.asList(
            new PropertyNameAnalyzer(),
            new PropertyDescriptionAnalyzer(),
            new PropertyTypeAnalyzer(),
            new PropertyWhenAnalyzer(),
            new PropertyHintAnalyzer(),
            new PropertyExampleAnalyzer(),
            new PropertyDefaultAnalyzer(),
            new PropertyDisplayNameAnalyzer(),
            new PropertyInitValueAnalyzer(),
            new PropertyScriptSignatureAnalyzer(),
            new PropertyAutoCompleteContributorAnalyzer());

    public static PropertyDescriptor analyze(FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        PropertyDescriptor.Builder builder = PropertyDescriptor.builder();
        FIELD_INFO_ANALYZERS.forEach(handler -> handler.handle(fieldInfo, builder, context));
        return builder.build();
    }
}
