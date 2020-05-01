package com.reedelk.module.descriptor.analyzer;

import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.analyzer.commons.AssetUtils;
import com.reedelk.module.descriptor.analyzer.commons.Messages.Scan;
import com.reedelk.module.descriptor.analyzer.commons.ReadModuleDescriptor;
import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzer;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerFactory;
import com.reedelk.module.descriptor.analyzer.type.TypeAnalyzer;
import com.reedelk.module.descriptor.json.JsonProvider;
import com.reedelk.module.descriptor.model.ModuleDescriptor;
import com.reedelk.module.descriptor.model.component.ComponentDescriptor;
import com.reedelk.module.descriptor.model.component.ComponentType;
import com.reedelk.module.descriptor.model.type.TypeDescriptor;
import com.reedelk.runtime.api.annotation.Module;
import com.reedelk.runtime.api.annotation.ModuleComponent;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ModuleDescriptorAnalyzer {

    /**
     * Used by IntelliJ designer to load components definitions from a given Jar. If the Jar
     * contains the pre-generated component's descriptors JSON file then it is used to create
     * the ModuleDescriptor object containing metadata about the Components. Otherwise the
     * classes in the Jar package are analyze to extract infos about the components provided in the module.
     * A target JAR might not have a module descriptor.
     *
     * @param targetJarPath the  target path of the Jar from which we want to retrieve the module descriptor info.
     * @return the ModuleDescriptor.
     * @throws ModuleDescriptorException if the JSON could not be de-serialized.
     */
    public Optional<ModuleDescriptor> from(String targetJarPath) {
        return ReadModuleDescriptor.from(targetJarPath).map(json -> {
            try {
                ModuleDescriptor moduleDescriptor = JsonProvider.fromJson(json);
                moduleDescriptor.getComponents().forEach(componentDescriptor -> {
                    // Load icons and images
                    Icon componentIcon = AssetUtils.loadIcon(targetJarPath, componentDescriptor.getFullyQualifiedName());
                    componentDescriptor.setIcon(componentIcon);
                    Image componentImage = AssetUtils.loadImage(targetJarPath, componentDescriptor.getFullyQualifiedName());
                    componentDescriptor.setImage(componentImage);
                });
                return moduleDescriptor;
            } catch (Exception exception) {
                throw new ModuleDescriptorException(exception);
            }
        });
    }

    /**
     * Used by reedelk-maven-plugin. Once the Descriptor has been returned, it serializes the Object into a JSON file
     * into the module/resources directory so that it can later be used by the IntelliJ Designer to load component's
     * definitions.
     *
     * @param directory  the target/classes folder of the compiled module.
     * @param moduleName the name of the module.
     * @return the ModuleDescriptor build out of the annotations found in the compiled classes.
     */
    public ModuleDescriptor fromDirectory(String directory, String moduleName, boolean resolveImages) {
        try {
            ScanResult scanResult = instantiateScanner()
                    .overrideClasspath(directory)
                    .scan();
            ModuleDescriptor moduleDescriptor = analyzeFrom(scanResult, moduleName);
            if (resolveImages) {
                moduleDescriptor.getComponents().forEach(componentDescriptor -> {
                    String componentFullyQualifiedName = componentDescriptor.getFullyQualifiedName();
                    componentDescriptor.setImage(AssetUtils.loadImageFromBaseDirectory(directory, componentFullyQualifiedName));
                    componentDescriptor.setIcon(AssetUtils.loadIconFromBaseDirectory(directory, componentFullyQualifiedName));
                });
            }
            return moduleDescriptor;
        } catch (Exception exception) {
            throw new ModuleDescriptorException(exception);
        }
    }

    /**
     * Used by IntelliJ designer to load info about system components. The system components are a dependency imported
     * in the IntelliJ plugin, hence we use the classloader to load the resources instead of accessing the .jar file.
     */
    public ModuleDescriptor from(Class<?> systemComponentClazz) {
        URL resource = systemComponentClazz.getClassLoader().getResource(ModuleDescriptor.RESOURCE_FILE_NAME);
        ModuleDescriptor moduleDescriptor = JsonProvider.fromURL(resource);
        // Load images
        moduleDescriptor.getComponents().forEach(componentDescriptor -> {
            URL iconURL = systemComponentClazz.getClassLoader().getResource(componentDescriptor.getFullyQualifiedName() + "-icon.png");
            componentDescriptor.setIcon(AssetUtils.getIcon(iconURL));
            URL imageURL = systemComponentClazz.getClassLoader().getResource(componentDescriptor.getFullyQualifiedName() + ".png");
            componentDescriptor.setImage(AssetUtils.getImage(imageURL));
        });
        return moduleDescriptor;
    }

    private ModuleDescriptor analyzeFrom(final ScanResult scanResult, final String moduleName) {
        String moduleDisplayName = moduleDisplayNameOf(scanResult, moduleName);
        List<ComponentDescriptor> components = componentsOf(scanResult);
        List<TypeDescriptor> types = typesOf(scanResult);

        ModuleDescriptor moduleDescriptor = new ModuleDescriptor();
        moduleDescriptor.setDisplayName(moduleDisplayName);
        moduleDescriptor.setComponents(components);
        moduleDescriptor.setName(moduleName);
        moduleDescriptor.setTypes(types);
        return moduleDescriptor;
    }

    private List<TypeDescriptor> typesOf(ScanResult scanResult) {
        TypeAnalyzer analyzer = new TypeAnalyzer(scanResult);
        return analyzer.analyze();
    }

    private String moduleDisplayNameOf(ScanResult scanResult, String moduleName) {
        ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(Module.class.getName());
        return classInfoList.stream()
                .findFirst()
                .map(classInfo -> ScannerUtils.annotationValueOrDefaultFrom(classInfo, Module.class, moduleName))
                .orElse(moduleName);
    }
    private List<ComponentDescriptor> componentsOf(ScanResult scanResult) {
        ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(ModuleComponent.class.getName());
        return classInfoList.stream()
                .map(classInfo -> {
                    try {
                        ComponentAnalyzer componentAnalyzer = ComponentAnalyzerFactory.get(scanResult);
                        return componentAnalyzer.analyze(classInfo);
                    } catch (Exception exception) {
                        String message = Scan.ERROR_SCAN_COMPONENT.format(classInfo.getName(), exception.getMessage());
                        throw new ModuleDescriptorException(message, exception);
                    }
                })
                .filter(descriptor -> !ComponentType.UNKNOWN.equals(descriptor.getType()))
                .collect(toList());
    }

    private static ScanResult scanResultFrom(String targetPath) {
        return instantiateScanner()
                .overrideClasspath(targetPath)
                .scan();
    }

    private static ClassGraph instantiateScanner() {
        return new ClassGraph()
                .enableFieldInfo()
                .enableMethodInfo()
                .enableAnnotationInfo()
                .ignoreFieldVisibility();
    }

    public ModuleDescriptor fromPackage(String... whiteListPackages) {
        try {
            ScanResult scanResult = instantiateScanner()
                    .whitelistPackages(whiteListPackages)
                    .scan();
            return analyzeFrom(scanResult, "flow-control");
        } catch (Exception e) {
            throw new ModuleDescriptorException(e);
        }
    }
}
