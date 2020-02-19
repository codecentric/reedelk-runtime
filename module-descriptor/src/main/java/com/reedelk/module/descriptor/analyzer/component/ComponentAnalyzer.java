package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.property.ComponentPropertyAnalyzer;
import com.reedelk.module.descriptor.model.ComponentDescriptor;
import com.reedelk.module.descriptor.model.ComponentPropertyDescriptor;
import com.reedelk.module.descriptor.model.ComponentType;
import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.commons.StringUtils;
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
        String description = getComponentDescription(classInfo);
        ComponentType componentType = getComponentType(classInfo);
        List<ComponentPropertyDescriptor> propertiesDescriptor = analyzeProperties(classInfo);
        return ComponentDescriptor.create()
                .displayName(displayName)
                .description(description)
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

    // The ClassInfo component descriptor *must* have the ModuleComponent annotation if we get here.
    private String getComponentDisplayName(ClassInfo componentClassInfo) {
        return ScannerUtils.annotationParameterValueOrDefaultFrom(
                componentClassInfo, ModuleComponent.class, "name", componentClassInfo.getSimpleName());
    }

    // The ClassInfo component descriptor *must* have the ModuleComponent annotation if we get here.
    private String getComponentDescription(ClassInfo componentClassInfo) {
        return ScannerUtils.annotationParameterValueOrDefaultFrom(
                componentClassInfo, ModuleComponent.class, "description", null);
    }

    private ComponentType getComponentType(ClassInfo classInfo) {
        ComponentTypeAnalyzer analyzer = new ComponentTypeAnalyzer(classInfo);
        return analyzer.analyze();
    }
}
