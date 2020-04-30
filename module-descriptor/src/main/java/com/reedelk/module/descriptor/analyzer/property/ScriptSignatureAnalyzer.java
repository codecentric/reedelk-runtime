package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import com.reedelk.module.descriptor.model.property.ScriptSignatureDescriptor;
import com.reedelk.runtime.api.annotation.ScriptSignature;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.FieldInfo;

import java.util.List;

public class ScriptSignatureAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        if (ScannerUtils.hasAnnotation(fieldInfo, ScriptSignature.class)) {
            AnnotationInfo info = fieldInfo.getAnnotationInfo(ScriptSignature.class.getName());
            // TODO: Test this. also you should create a pair, with signature arg name -> arg type
            List<String> functionSignatureArguments = ScannerUtils.stringListParameterValueFrom(info, "arguments");
            List<String> functionSignatureArgumentTypes = ScannerUtils.stringListParameterValueFrom(info, "types");

            ScriptSignatureDescriptor definition = new ScriptSignatureDescriptor();
            definition.setArguments(functionSignatureArguments);
            definition.setTypes(functionSignatureArgumentTypes);
            builder.scriptSignature(definition);
        }
    }
}
