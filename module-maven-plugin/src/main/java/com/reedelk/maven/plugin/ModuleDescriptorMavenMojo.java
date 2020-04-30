package com.reedelk.maven.plugin;

import com.reedelk.module.descriptor.analyzer.ModuleDescriptorAnalyzer;
import com.reedelk.module.descriptor.json.JsonProvider;
import com.reedelk.module.descriptor.model.ModuleDescriptor;
import com.reedelk.runtime.api.RuntimeApi;
import com.reedelk.runtime.api.commons.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

@Mojo(name="generate-module-descriptor", defaultPhase=LifecyclePhase.PROCESS_CLASSES)
public class ModuleDescriptorMavenMojo extends AbstractMojo {

    @Parameter( property = "project.build.outputDirectory")
    private String compiledClasses;
    @Parameter(property = "project.build.resources[0].directory")
    private String resourcesDirectory;
    @Parameter(property = "project.name")
    private String projectName;
    @Parameter(property = "moduleName")
    private String moduleName;
    @Parameter(property = "scanApi")
    private boolean scanApi;

    @Override
    public void execute() throws MojoFailureException {
        ModuleDescriptorAnalyzer analyzer = new ModuleDescriptorAnalyzer();
        try {

            String finalModuleName = StringUtils.isBlank(moduleName) ? projectName : moduleName;

            // Used by runtime-commons to also scan classes from runtime API (e.g: Message, FlowContext).
            if (scanApi) {
                // Scan API
                ModuleDescriptor scanApiModuleDescriptor = analyzer.fromPackage(RuntimeApi.class.getPackage().getName());

                // Scan Module
                ModuleDescriptor moduleDescriptor = analyzer.fromDirectory(compiledClasses, finalModuleName, false);
                moduleDescriptor.getComponents().addAll(scanApiModuleDescriptor.getComponents());
                moduleDescriptor.getTypes().addAll(scanApiModuleDescriptor.getTypes());

                // We only write if there are components, functions and types defined.
                if (shouldWriteFile(moduleDescriptor)) {
                    writeAsJson(moduleDescriptor);
                }

            } else {

                ModuleDescriptor moduleDescriptor = analyzer.fromDirectory(compiledClasses, finalModuleName, false);

                // We only write if there are components, functions and types defined.
                if (shouldWriteFile(moduleDescriptor)) {
                    writeAsJson(moduleDescriptor);
                }
            }

        } catch (Exception error) {
            String errorMessage = format("Could not build components definitions: %s", error.getMessage());
            throw new MojoFailureException(errorMessage, error);
        }
    }

    /**
     * Serialize the ModuleDescriptor as JSON and write it to the given path.
     */
    private void writeAsJson(ModuleDescriptor moduleDescriptor) throws IOException {
        String resultJson = JsonProvider.toJson(moduleDescriptor);

        Path path = Paths.get(resourcesDirectory, ModuleDescriptor.RESOURCE_FILE_NAME);
        byte[] strToBytes = resultJson.getBytes();
        Files.write(path, strToBytes);

        getLog().info("Module Descriptor written on: " + path.toString());
    }

    // We only write the file if the module defines some components or types.
    // otherwise it is just a module containing flows and subflows.
    private static boolean shouldWriteFile(ModuleDescriptor descriptor) {
        return !descriptor.getComponents().isEmpty() ||
                !descriptor.getTypes().isEmpty();
    }
}
