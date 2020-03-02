package com.reedelk.module.descriptor.analyzer;

import com.reedelk.module.descriptor.ModuleDescriptor;
import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.analyzer.autocomplete.AutocompleteItemAnalyzer;
import com.reedelk.module.descriptor.analyzer.autocomplete.AutocompleteTypeAnalyzer;
import com.reedelk.module.descriptor.analyzer.commons.AssetUtils;
import com.reedelk.module.descriptor.analyzer.commons.ComponentDescriptorsJsonFile;
import com.reedelk.module.descriptor.analyzer.commons.Messages;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzer;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerFactory;
import com.reedelk.module.descriptor.json.JsonProvider;
import com.reedelk.module.descriptor.model.AutocompleteItemDescriptor;
import com.reedelk.module.descriptor.model.AutocompleteTypeDescriptor;
import com.reedelk.module.descriptor.model.ComponentDescriptor;
import com.reedelk.module.descriptor.model.ComponentType;
import com.reedelk.runtime.api.annotation.ModuleComponent;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ModuleDescriptorAnalyzer {

    private static final Logger LOG = Logger.getLogger(ModuleDescriptorAnalyzer.class);

    /**
     * Used by IntelliJ designer to load components definitions from a given Jar. If the Jar
     * contains the pre-generated component's descriptors JSON file then it is used to create
     * the ModuleDescriptor object containing metadata about the Components. Otherwise the
     * classes in the Jar package are analyze to extract infos about the components provided in the module.
     *
     * @param targetJarPath the  target path of the Jar from which we want to retrieve the module descriptor info.
     * @return the ModuleDescriptor.
     * @throws ModuleDescriptorException if the JSON could not be de-serialized.
     */
    public ModuleDescriptor from(String targetJarPath, String moduleName) throws ModuleDescriptorException {
        Optional<String> componentsDefinitionFromJar = ComponentDescriptorsJsonFile.from(targetJarPath);

        LOG.info(format("Analyzing Module from target Jar path=[%s]. Component Descriptors Json file found=[%s] ",
                targetJarPath, componentsDefinitionFromJar.isPresent()));

        if (componentsDefinitionFromJar.isPresent()) {
            String json = componentsDefinitionFromJar.get();
            try {
                ModuleDescriptor moduleDescriptor = JsonProvider.fromJson(json);

                // Load icons and images
                moduleDescriptor.getComponents().forEach(componentDescriptor -> {
                    Icon componentIcon = AssetUtils.loadIcon(targetJarPath, componentDescriptor.getFullyQualifiedName());
                    componentDescriptor.setIcon(componentIcon);

                    Image componentImage = AssetUtils.loadImage(targetJarPath, componentDescriptor.getFullyQualifiedName());
                    componentDescriptor.setImage(componentImage);
                });

                return moduleDescriptor;
            } catch (Exception e) {
                throw new ModuleDescriptorException(e);
            }

        } else {

            try {
                // Here there should be a strategy. If the target Jar path contains
                // the json with the components definitions, then use that one, otherwise scan.
                ScanResult scanResult = scanResultFrom(targetJarPath);
                ModuleDescriptor moduleDescriptor = analyzeFrom(scanResult, moduleName);

                // Load icons and images
                moduleDescriptor.getComponents().forEach(componentDescriptor -> {
                    Icon componentIcon = AssetUtils.loadIcon(scanResult, componentDescriptor.getFullyQualifiedName());
                    componentDescriptor.setIcon(componentIcon);

                    Image componentImage = AssetUtils.loadImage(scanResult, componentDescriptor.getFullyQualifiedName());
                    componentDescriptor.setImage(componentImage);
                });

                return moduleDescriptor;
            } catch (Exception e) {
                throw new ModuleDescriptorException(e);
            }
        }
    }

    /**
     * Used by reedelk-maven-plugin. Once the Descriptor has been returned, it serializes the Object into a JSON file
     * into the module/resources directory so that it can later be used by the IntelliJ Designer to load component's
     * definitions.
     *
     *
     * @param directory the target/classes folder of the compiled module.
     * @param moduleName the name of the module.
     * @return the ModuleDescriptor build out of the annotations found in the compiled classes.
     */
    public ModuleDescriptor fromDirectory(String directory, String moduleName, boolean resolveImages) throws ModuleDescriptorException {
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
        } catch (Exception e) {
            throw new ModuleDescriptorException(e);
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
            if (iconURL != null) {
                Icon componentIcon = AssetUtils.getIcon(iconURL);
                componentDescriptor.setIcon(componentIcon);
            }

            URL imageURL = systemComponentClazz.getClassLoader().getResource(componentDescriptor.getFullyQualifiedName() + ".png");
            if (imageURL != null) {
                Image componentImage = AssetUtils.getImage(imageURL);
                componentDescriptor.setImage(componentImage);
            }
        });
        return moduleDescriptor;
    }

    private ModuleDescriptor analyzeFrom(final ScanResult scanResult, final String moduleName) {
        List<ComponentDescriptor> allComponentDescriptors = componentDescriptorsFrom(scanResult);
        List<ComponentDescriptor> knownComponentDescriptors = filterOutUnknownClassComponents(allComponentDescriptors);
        List<AutocompleteTypeDescriptor> autocompleteTypes = analyzeAutocompleteTypes(scanResult);
        List<AutocompleteItemDescriptor> autocompleteItems = analyzeAutocompleteItems(scanResult);
        ModuleDescriptor moduleDescriptor = new ModuleDescriptor();
        moduleDescriptor.setName(moduleName);
        moduleDescriptor.setComponents(knownComponentDescriptors);
        moduleDescriptor.setAutocompleteTypes(autocompleteTypes);
        moduleDescriptor.setAutocompleteItems(autocompleteItems);
        return moduleDescriptor;
    }

    private List<AutocompleteTypeDescriptor> analyzeAutocompleteTypes(ScanResult scanResult) {
        AutocompleteTypeAnalyzer analyzer = new AutocompleteTypeAnalyzer(scanResult);
        return analyzer.analyze();
    }

    private List<AutocompleteItemDescriptor> analyzeAutocompleteItems(ScanResult scanResult) {
        AutocompleteItemAnalyzer autocompleteItemAnalyzer = new AutocompleteItemAnalyzer(scanResult);
        return autocompleteItemAnalyzer.analyze();
    }

    private List<ComponentDescriptor> componentDescriptorsFrom(ScanResult scanResult) {
        ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(ModuleComponent.class.getName());
        List<ComponentDescriptor> componentDescriptors = new ArrayList<>();
        classInfoList.forEach(classInfo -> {
            try {
                ComponentAnalyzer componentAnalyzer = ComponentAnalyzerFactory.get(scanResult);
                ComponentDescriptor descriptor = componentAnalyzer.analyze(classInfo);
                componentDescriptors.add(descriptor);
            } catch (Exception exception) {
                String message = format(Messages.ERROR_SCAN_COMPONENT, classInfo.getName(), exception.getMessage());
                LOG.error(message, exception);
            }
        });
        return componentDescriptors;
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

    private static List<ComponentDescriptor> filterOutUnknownClassComponents(List<ComponentDescriptor> componentDescriptorList) {
        return componentDescriptorList.stream()
                .filter(descriptor -> !ComponentType.UNKNOWN.equals(descriptor.getType()))
                .collect(Collectors.toList());
    }

    public ModuleDescriptor fromPackage(String ...whiteListPackages) throws ModuleDescriptorException {
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
