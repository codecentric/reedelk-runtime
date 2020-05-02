package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.model.component.ComponentInputDescriptor;
import com.reedelk.runtime.api.annotation.ComponentInput;
import com.reedelk.runtime.api.commons.StringUtils;
import io.github.classgraph.AnnotationClassRef;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassInfo;

import java.util.List;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.hasAnnotation;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ComponentInputAnalyzer {

    private final ClassInfo classInfo;

    public ComponentInputAnalyzer(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }
    private static final Object[] EMPTY = new Object[] {};

    public ComponentInputDescriptor analyze() {
        if (!hasAnnotation(classInfo, ComponentInput.class)) return null;

        AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(ComponentInput.class.getName());
        List<String> inputPayload = getInputPayload(annotationInfo);
        String description = ScannerUtils.parameterValueFrom("description", StringUtils.EMPTY, annotationInfo);

        ComponentInputDescriptor descriptor = new ComponentInputDescriptor();
        descriptor.setDescription(description);
        descriptor.setPayload(inputPayload);
        return descriptor;
    }

    private List<String> getInputPayload(AnnotationInfo annotationInfo) {
        Object[] payload = ScannerUtils.parameterValueFrom("payload", EMPTY, annotationInfo);
        return stream(payload)
                .map(annotationClassRef -> ((AnnotationClassRef) annotationClassRef).getName())
                .collect(toList());
    }
}
