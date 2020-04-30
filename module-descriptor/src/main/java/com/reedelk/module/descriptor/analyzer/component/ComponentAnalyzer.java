package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.property.PropertyAnalyzer;
import com.reedelk.module.descriptor.model.component.ComponentDescriptor;
import com.reedelk.module.descriptor.model.component.ComponentType;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import com.reedelk.runtime.api.annotation.Description;
import com.reedelk.runtime.api.annotation.ModuleComponent;
import io.github.classgraph.ClassInfo;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ComponentAnalyzer {

    private final PropertyAnalyzer propertyAnalyzer;

    public ComponentAnalyzer(PropertyAnalyzer propertyAnalyzer) {
        this.propertyAnalyzer = propertyAnalyzer;
    }

    public ComponentDescriptor analyze(ClassInfo classInfo) {
        String displayName = getComponentDisplayName(classInfo);
        String description = getComponentDescription(classInfo);
        ComponentType componentType = getComponentType(classInfo);
        List<PropertyDescriptor> propertiesDescriptor = analyzeProperties(classInfo);
        return ComponentDescriptor.create()
                .displayName(displayName)
                .description(description)
                .hidden(ScannerUtils.isHidden(classInfo))
                .type(componentType)
                .fullyQualifiedName(classInfo.getName())
                .propertyDescriptors(propertiesDescriptor)
                .build();
    }

    private List<PropertyDescriptor> analyzeProperties(ClassInfo classInfo) {
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
        return ScannerUtils.annotationValueOrDefaultFrom(
                componentClassInfo, ModuleComponent.class, componentClassInfo.getSimpleName());
    }

    // The ClassInfo component descriptor *must* have the ModuleComponent annotation if we get here.
    private String getComponentDescription(ClassInfo componentClassInfo) {
        return ScannerUtils.annotationValueOrDefaultFrom(
                componentClassInfo, Description.class, null);
    }

    private ComponentType getComponentType(ClassInfo classInfo) {
        ComponentTypeAnalyzer analyzer = new ComponentTypeAnalyzer(classInfo);
        return analyzer.analyze();
    }
}
