package com.reedelk.maven.plugin;

import com.reedelk.module.descriptor.ModuleDescriptor;
import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.analyzer.ModuleDescriptorAnalyzer;
import com.reedelk.module.descriptor.json.JsonProvider;
import com.reedelk.runtime.api.RuntimeApi;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.message.Message;
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

            // TODO: Improve this code.
            ModuleDescriptor scanApiModuleDescriptor = null;
            if (scanApi) {
                // Scan API
                scanApiModuleDescriptor = analyzer.fromPackage(RuntimeApi.class.getPackage().getName());

            }
            String finalModuleName = StringUtils.isBlank(moduleName) ? projectName : moduleName;

            // Scan the classes
            ModuleDescriptor moduleDescriptor = analyzer.fromDirectory(compiledClasses, finalModuleName, false);

            // We only create the module descriptor if there are any components or autocomplete items.
            if (!moduleDescriptor.getComponents().isEmpty() ||
                    !moduleDescriptor.getAutocompleteItems().isEmpty() ||
                    scanApi) {

                if (scanApiModuleDescriptor != null) {
                    moduleDescriptor.getAutocompleteItems().addAll(scanApiModuleDescriptor.getAutocompleteItems());
                    moduleDescriptor.getComponents().addAll(scanApiModuleDescriptor.getComponents());
                }

                writeAsJson(moduleDescriptor);
            }
        } catch (Exception e) {
            String errorMessage = format("Could not build components definitions: %s", e.getMessage());
            throw new MojoFailureException(errorMessage, e);
        }
    }

    /**
     * Serialize the ModuleDescriptor as JSON and write it to the given path.
     */
    private void writeAsJson(ModuleDescriptor moduleDescriptor) throws ModuleDescriptorException, IOException {
        String resultJson = JsonProvider.toJson(moduleDescriptor);

        Path path = Paths.get(resourcesDirectory, ModuleDescriptor.RESOURCE_FILE_NAME);
        byte[] strToBytes = resultJson.getBytes();
        Files.write(path, strToBytes);

        getLog().info("Module Descriptor written on: " + path.toString());
    }
}
