package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import com.reedelk.module.descriptor.model.property.ScriptSignatureArgument;
import com.reedelk.module.descriptor.model.property.ScriptSignatureDescriptor;
import com.reedelk.runtime.api.annotation.ScriptSignature;
import io.github.classgraph.AnnotationClassRef;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.FieldInfo;

import java.util.ArrayList;
import java.util.List;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.getParameterValue;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ScriptSignatureAnalyzer implements FieldInfoAnalyzer {

    private static final Object[] EMPTY = new Object[] {};

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        if (!ScannerUtils.hasAnnotation(fieldInfo, ScriptSignature.class)) return;

        AnnotationInfo info = fieldInfo.getAnnotationInfo(ScriptSignature.class.getName());

        List<String> functionSignatureArguments = ScannerUtils.stringListParameterValueFrom(info, "arguments");
        List<String> functionSignatureArgumentTypes = getArgumentTypes(info);

        if (functionSignatureArguments.size() != functionSignatureArgumentTypes.size()) {
            throw new ModuleDescriptorException("Script signature must have the same number of arguments/types pairs");
        }

        List<ScriptSignatureArgument> arguments = new ArrayList<>();
        for (int i = 0; i < functionSignatureArguments.size(); i++) {
            arguments.add(new ScriptSignatureArgument(functionSignatureArguments.get(i), functionSignatureArgumentTypes.get(i)));
        }
        ScriptSignatureDescriptor definition = new ScriptSignatureDescriptor();
        definition.setArguments(arguments);
        builder.scriptSignature(definition);
    }

    private List<String> getArgumentTypes(AnnotationInfo annotationInfo) {
        Object[] payload = getParameterValue("types", EMPTY, annotationInfo);
        return stream(payload)
                .map(annotationClassRef -> ((AnnotationClassRef) annotationClassRef).getName())
                .collect(toList());
    }
}
