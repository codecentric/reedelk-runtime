package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.model.component.ComponentOutputDescriptor;
import com.reedelk.runtime.api.annotation.ComponentOutput;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.message.MessageAttributes;
import io.github.classgraph.AnnotationClassRef;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassInfo;

import java.util.List;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.hasAnnotation;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class ComponentOutputAnalyzer {

    private static final Object[] EMPTY = new Object[]{};
    private static final List<String> DEFAULT_ATTRIBUTES = singletonList(MessageAttributes.class.getName());

    private final ClassInfo classInfo;

    public ComponentOutputAnalyzer(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public ComponentOutputDescriptor analyze() {
        if (!hasAnnotation(classInfo, ComponentOutput.class)) return null;

        AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(ComponentOutput.class.getName());

        String description = ScannerUtils.parameterValueFrom(annotationInfo, "description", StringUtils.EMPTY);
        List<String> attributesType = getOutputAttributes(annotationInfo);
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
        Object[] payload = ScannerUtils.parameterValueFrom(annotationInfo, "payload", EMPTY);
        return stream(payload)
                .map(annotationClassRef -> ((AnnotationClassRef) annotationClassRef).getName())
                .collect(toList());
    }

    private List<String> getOutputAttributes(AnnotationInfo annotationInfo) {
        Object[] attributes = ScannerUtils.parameterValueFrom(annotationInfo, "attributes", EMPTY);
        return attributes.length == 0 ?
                DEFAULT_ATTRIBUTES :
                stream(attributes)
                        .map(annotationClassRef -> ((AnnotationClassRef) annotationClassRef).getName())
                        .collect(toList());
    }
}
