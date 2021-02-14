package de.codecentric.reedelk.module.descriptor.analyzer.component;

import de.codecentric.reedelk.module.descriptor.ModuleDescriptorException;
import de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import de.codecentric.reedelk.module.descriptor.model.component.ComponentOutputDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.ComponentOutput;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;
import io.github.classgraph.AnnotationClassRef;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassInfo;

import java.util.List;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.hasAnnotation;
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
        String dynamicPropertyName = ScannerUtils.parameterValueFrom(annotationInfo, "dynamicPropertyName", StringUtils.EMPTY);
        List<String> attributesType = getOutputAttributes(annotationInfo);
        List<String> outputPayload = getOutputPayload(annotationInfo);
        if (outputPayload.isEmpty()) {
            String error = format("Component Output payload types must not be empty (class: %s).", classInfo.getName());
            throw new ModuleDescriptorException(error);
        }

        ComponentOutputDescriptor descriptor = new ComponentOutputDescriptor();
        descriptor.setDynamicPropertyName(dynamicPropertyName);
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
