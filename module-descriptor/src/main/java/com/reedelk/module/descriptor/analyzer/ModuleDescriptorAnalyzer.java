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
import com.reedelk.runtime.api.RuntimeApi;
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

import static com.reedelk.module.descriptor.analyzer.commons.Messages.Analyzer.*;
import static java.util.stream.Collectors.toList;

public class ModuleDescriptorAnalyzer {

    // Used by IntelliJ flow designer to read components definitions from a given jar.
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
            } catch (ModuleDescriptorException exception) {
                throw exception;
            } catch (Exception exception) {
                String error = ERROR_FROM_JAR_PATH.format(targetJarPath, exception.getMessage());
                throw new ModuleDescriptorException(error, exception);
            }
        });
    }

    // Used by maven-plugin to create module descriptor from compiled classes in the given directory.
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
        } catch (ModuleDescriptorException exception) {
            throw exception;
        } catch (Exception exception) {
            String error = ERROR_FROM_DIRECTORY.format(directory, moduleName, resolveImages, exception.getMessage());
            throw new ModuleDescriptorException(error, exception);
        }
    }

    // Used by IntelliJ flow designer to read system components module descriptor definitions.
    // The module descriptor is accessed using the *classpath* which loaded any of the system component classes.
    public ModuleDescriptor from(Class<?> systemComponentClazz) {
        URL resource = systemComponentClazz.getClassLoader().getResource(ModuleDescriptor.RESOURCE_FILE_NAME);
        ModuleDescriptor moduleDescriptor = JsonProvider.fromURL(resource);
        moduleDescriptor.getComponents().forEach(componentDescriptor -> {
            // Load images
            URL iconURL = systemComponentClazz.getClassLoader().getResource(componentDescriptor.getFullyQualifiedName() + "-icon.png");
            componentDescriptor.setIcon(AssetUtils.getIcon(iconURL));
            URL imageURL = systemComponentClazz.getClassLoader().getResource(componentDescriptor.getFullyQualifiedName() + ".png");
            componentDescriptor.setImage(AssetUtils.getImage(imageURL));
        });
        return moduleDescriptor;
    }

    // Returns the scanned types from the runtime api package. For instance it scans annotations
    // on FlowContext, Message and other runtime api defined types.
    public List<TypeDescriptor> scanRuntimeApiTypes() {
        try {
            ScanResult scanResult = instantiateScanner()
                    .whitelistPackages(RuntimeApi.class.getPackage().getName())
                    .scan();
            return typesOf(scanResult);
        } catch (ModuleDescriptorException exception) {
            throw exception;
        } catch (Exception exception) {
            String error = ERROR_SCAN_API_TYPES.format(exception.getMessage());
            throw new ModuleDescriptorException(error, exception);
        }
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

    private static ClassGraph instantiateScanner() {
        return new ClassGraph()
                .enableFieldInfo()
                .enableMethodInfo()
                .enableAnnotationInfo()
                .ignoreFieldVisibility();
    }
}
