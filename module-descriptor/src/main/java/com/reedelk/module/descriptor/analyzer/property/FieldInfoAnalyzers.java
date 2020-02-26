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
            new NameAnalyzer(),
            new DescriptionAnalyzer(),
            new TypeAnalyzer(),
            new WhenAnalyzer(),
            new HintAnalyzer(),
            new ExampleAnalyzer(),
            new DefaultValueAnalyzer(),
            new DisplayNameAnalyzer(),
            new InitValueAnalyzer(),
            new ScriptSignatureAnalyzer());

    public static PropertyDescriptor analyze(FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        PropertyDescriptor.Builder builder = PropertyDescriptor.builder();
        FIELD_INFO_ANALYZERS.forEach(handler -> handler.handle(fieldInfo, builder, context));
        return builder.build();
    }
}
