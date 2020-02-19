package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.ComponentPropertyDescriptor;
import com.reedelk.module.descriptor.model.ScriptSignatureDescriptor;
import com.reedelk.runtime.api.annotation.ScriptSignature;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.FieldInfo;

import java.util.List;

public class PropertyScriptSignatureAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo propertyInfo, ComponentPropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        if (ScannerUtils.hasAnnotation(propertyInfo, ScriptSignature.class)) {
            AnnotationInfo info = propertyInfo.getAnnotationInfo(ScriptSignature.class.getName());
            List<String> functionSignatureArguments = ScannerUtils.stringListParameterValueFrom(info, "arguments");
            ScriptSignatureDescriptor definition = new ScriptSignatureDescriptor();
            definition.setArguments(functionSignatureArguments);
            builder.scriptSignature(definition);
        }
    }
}
