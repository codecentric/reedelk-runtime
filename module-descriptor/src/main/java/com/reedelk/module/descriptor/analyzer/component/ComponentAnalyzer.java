package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.property.ComponentPropertyAnalyzer;
import com.reedelk.module.descriptor.model.ComponentDescriptor;
import com.reedelk.module.descriptor.model.ComponentPropertyDescriptor;
import com.reedelk.module.descriptor.model.ComponentType;
import com.reedelk.runtime.api.annotation.ModuleComponent;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValueList;
import io.github.classgraph.ClassInfo;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ComponentAnalyzer {

    private final ComponentPropertyAnalyzer propertyAnalyzer;

    public ComponentAnalyzer(ComponentPropertyAnalyzer propertyAnalyzer) {
        this.propertyAnalyzer = propertyAnalyzer;
    }

    public ComponentDescriptor analyze(ClassInfo classInfo) {
        String displayName = getComponentDisplayName(classInfo);
        ComponentType componentType = getComponentType(classInfo);
        List<ComponentPropertyDescriptor> propertiesDescriptor = analyzeProperties(classInfo);
        return ComponentDescriptor.create()
                .displayName(displayName)
                .hidden(ScannerUtils.isHidden(classInfo))
                .componentType(componentType)
                .fullyQualifiedName(classInfo.getName())
                .propertyDescriptors(propertiesDescriptor)
                .build();
    }

    private List<ComponentPropertyDescriptor> analyzeProperties(ClassInfo classInfo) {
        return classInfo
                .getFieldInfo()
                .stream()
                .map(propertyAnalyzer::analyze)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private String getComponentDisplayName(ClassInfo componentClassInfo) {
        // The ClassInfo component descriptor *must* have the ESBComponent annotation if we get here.
        AnnotationInfo componentDisplayName = componentClassInfo.getAnnotationInfo(ModuleComponent.class.getName());
        AnnotationParameterValueList parameterValues = componentDisplayName.getParameterValues();
        return parameterValues.containsName("value") ?
                (String) parameterValues.getValue("value") :
                componentClassInfo.getSimpleName();
    }

    private ComponentType getComponentType(ClassInfo classInfo) {
        ComponentTypeAnalyzer analyzer = new ComponentTypeAnalyzer(classInfo);
        return analyzer.analyze();
    }
}
