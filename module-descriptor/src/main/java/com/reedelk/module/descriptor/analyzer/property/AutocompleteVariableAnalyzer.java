package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.AutocompleteVariableDescriptor;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import com.reedelk.runtime.api.annotation.AutocompleteVariable;
import com.reedelk.runtime.api.annotation.AutocompleteVariables;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.FieldInfo;

public class AutocompleteVariableAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        ScannerUtils.repeatableAnnotation(fieldInfo, AutocompleteVariable.class, AutocompleteVariables.class).forEach(annotationInfo -> {
            AutocompleteVariableDescriptor descriptor = processAutocompleteVariableInfo(annotationInfo);
            builder.autocompleteVariable(descriptor);
        });
    }

    private AutocompleteVariableDescriptor processAutocompleteVariableInfo(AnnotationInfo info) {
        String name = ScannerUtils.stringParameterValueFrom(info, "name");
        String type = ScannerUtils.getParameterValue("type", info);

        String[] split = type.split("\\.");

        // We just keep the simple name of the Fully Qualified Class Name.
        String realType = split[split.length - 1];

        AutocompleteVariableDescriptor descriptor = new AutocompleteVariableDescriptor();
        descriptor.setName(name);
        descriptor.setType(realType);
        return descriptor;
    }
}
