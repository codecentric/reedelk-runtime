package de.codecentric.reedelk.module.descriptor.analyzer.component;

import de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import de.codecentric.reedelk.module.descriptor.analyzer.property.PropertyAnalyzer;
import de.codecentric.reedelk.module.descriptor.model.component.ComponentDescriptor;
import de.codecentric.reedelk.module.descriptor.model.component.ComponentInputDescriptor;
import de.codecentric.reedelk.module.descriptor.model.component.ComponentOutputDescriptor;
import de.codecentric.reedelk.module.descriptor.model.component.ComponentType;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.Description;
import de.codecentric.reedelk.runtime.api.annotation.ModuleComponent;
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
        ComponentInputDescriptor inputDescriptor = analyzeComponentInput(classInfo);
        ComponentOutputDescriptor outputDescriptor = analyzeComponentOutput(classInfo);
        return ComponentDescriptor.create()
                .hidden(ScannerUtils.isHidden(classInfo))
                .fullyQualifiedName(classInfo.getName())
                .properties(propertiesDescriptor)
                .displayName(displayName)
                .description(description)
                .output(outputDescriptor)
                .input(inputDescriptor)
                .type(componentType)
                .build();
    }

    private ComponentOutputDescriptor analyzeComponentOutput(ClassInfo classInfo) {
        ComponentOutputAnalyzer analyzer = new ComponentOutputAnalyzer(classInfo);
        return analyzer.analyze();
    }

    private ComponentInputDescriptor analyzeComponentInput(ClassInfo classInfo) {
        ComponentInputAnalyzer analyzer = new ComponentInputAnalyzer(classInfo);
        return analyzer.analyze();
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
        return ScannerUtils.annotationValueFrom(
                componentClassInfo, ModuleComponent.class, componentClassInfo.getSimpleName());
    }

    // The ClassInfo component descriptor *must* have the ModuleComponent annotation if we get here.
    private String getComponentDescription(ClassInfo componentClassInfo) {
        return ScannerUtils.annotationValueFrom(
                componentClassInfo, Description.class, null);
    }

    private ComponentType getComponentType(ClassInfo classInfo) {
        ComponentTypeAnalyzer analyzer = new ComponentTypeAnalyzer(classInfo);
        return analyzer.analyze();
    }
}
