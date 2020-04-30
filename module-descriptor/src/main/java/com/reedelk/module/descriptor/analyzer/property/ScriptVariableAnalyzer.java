package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import com.reedelk.module.descriptor.model.script.ScriptVariableDescriptor;
import com.reedelk.runtime.api.annotation.ScriptVariable;
import com.reedelk.runtime.api.annotation.ScriptVariables;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.FieldInfo;

public class ScriptVariableAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        ScannerUtils.repeatableAnnotation(fieldInfo, ScriptVariable.class, ScriptVariables.class).forEach(annotationInfo -> {
            ScriptVariableDescriptor descriptor = processScriptVariableAnnotation(annotationInfo);
            builder.scriptVariable(descriptor);
        });
    }

    private ScriptVariableDescriptor processScriptVariableAnnotation(AnnotationInfo info) {
        String name = ScannerUtils.stringParameterValueFrom(info, "name");
        String type = ScannerUtils.getParameterValue("type", info);

        String[] split = type.split("\\.");

        // We just keep the simple name of the Fully Qualified Class Name.
        String realType = split[split.length - 1];

        ScriptVariableDescriptor descriptor = new ScriptVariableDescriptor();
        descriptor.setName(name);
        descriptor.setType(realType);
        return descriptor;
    }
}
