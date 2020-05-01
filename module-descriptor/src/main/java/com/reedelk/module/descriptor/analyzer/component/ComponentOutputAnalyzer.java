package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.model.component.ComponentOutputDescriptor;
import com.reedelk.runtime.api.annotation.ComponentOutput;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.message.MessageAttributes;
import io.github.classgraph.AnnotationClassRef;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassInfo;

import java.util.List;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.getParameterValue;
import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.hasAnnotation;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ComponentOutputAnalyzer {

    private final ClassInfo classInfo;

    public ComponentOutputAnalyzer(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }
    private static final Object[] EMPTY = new Object[] {};

    public ComponentOutputDescriptor analyze() {
        if (!hasAnnotation(classInfo, ComponentOutput.class)) return null;

        AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(ComponentOutput.class.getName());

        String attributesType = getParameterValue("attributes", MessageAttributes.class.getName(), annotationInfo);
        String description = getParameterValue("description", StringUtils.EMPTY, annotationInfo);
        List<String> outputPayload = getOutputPayload(annotationInfo);
        if (outputPayload.isEmpty()) {
            String error = format("Component Output payload types must not be empty (class: %s).", classInfo.getName());
            throw new ModuleDescriptorException(error);
        }

        ComponentOutputDescriptor descriptor = new ComponentOutputDescriptor();
        descriptor.setAttributes(attributesType);
        descriptor.setDescription(description);
        descriptor.setPayload(outputPayload);
        return descriptor;
    }

    private List<String> getOutputPayload(AnnotationInfo annotationInfo) {
        Object[] payload = getParameterValue("payload", EMPTY, annotationInfo);
        return stream(payload)
                .map(annotationClassRef -> ((AnnotationClassRef) annotationClassRef).getName())
                .collect(toList());
    }
}
