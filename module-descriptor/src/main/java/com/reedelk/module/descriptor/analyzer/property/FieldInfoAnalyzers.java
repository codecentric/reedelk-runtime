package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import io.github.classgraph.FieldInfo;

import java.util.List;

import static java.util.Arrays.asList;

public class FieldInfoAnalyzers {

    private FieldInfoAnalyzers() {
    }

    private static final List<FieldInfoAnalyzer> FIELD_INFO_ANALYZERS =
            asList(new NameAnalyzer(),
                    new PropertyTypeAnalyzer(),
                    new WhenAnalyzer(),
                    new HintAnalyzer(),
                    new GroupAnalyzer(),
                    new ExampleAnalyzer(),
                    new InitValueAnalyzer(),
                    new MandatoryAnalyzer(),
                    new DescriptionAnalyzer(),
                    new DisplayNameAnalyzer(),
                    new DefaultValueAnalyzer(),
                    new ScriptSignatureAnalyzer());

    public static PropertyDescriptor analyze(FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        PropertyDescriptor.Builder builder = PropertyDescriptor.builder();
        FIELD_INFO_ANALYZERS.forEach(handler -> handler.handle(fieldInfo, builder, context));
        return builder.build();
    }
}
