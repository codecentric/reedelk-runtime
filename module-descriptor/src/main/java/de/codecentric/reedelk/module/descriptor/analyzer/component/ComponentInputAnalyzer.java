package de.codecentric.reedelk.module.descriptor.analyzer.component;

import de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import de.codecentric.reedelk.module.descriptor.model.component.ComponentInputDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.ComponentInput;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import io.github.classgraph.AnnotationClassRef;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassInfo;

import java.util.List;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.hasAnnotation;
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
        String description = ScannerUtils.parameterValueFrom(annotationInfo, "description", StringUtils.EMPTY);

        ComponentInputDescriptor descriptor = new ComponentInputDescriptor();
        descriptor.setDescription(description);
        descriptor.setPayload(inputPayload);
        return descriptor;
    }

    private List<String> getInputPayload(AnnotationInfo annotationInfo) {
        Object[] payload = ScannerUtils.parameterValueFrom(annotationInfo, "payload", EMPTY);
        return stream(payload)
                .map(annotationClassRef -> ((AnnotationClassRef) annotationClassRef).getName())
                .collect(toList());
    }
}
