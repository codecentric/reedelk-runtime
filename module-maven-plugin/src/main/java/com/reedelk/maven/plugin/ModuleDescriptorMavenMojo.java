package com.reedelk.maven.plugin;

import com.reedelk.module.descriptor.ModuleDescriptor;
import com.reedelk.module.descriptor.analyzer.ModuleAnalyzer;
import com.reedelk.module.descriptor.json.JsonProvider;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

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

    @Override
    public void execute() throws MojoFailureException {
        try {
            // Scan the classes
            ModuleAnalyzer analyzer = new ModuleAnalyzer();
            ModuleDescriptor moduleDescriptor = analyzer.fromClassesFolder(compiledClasses, false);

            // We only create the JSON
            if (!moduleDescriptor.getComponentDescriptors().isEmpty() ||
            !moduleDescriptor.getAutocompleteContributorDescriptors().isEmpty()) {

                // Convert to JSON
                String resultJson = JsonProvider.toJson(moduleDescriptor);

                // Write the result
                Path path = Paths.get(resourcesDirectory, ModuleDescriptor.RESOURCE_FILE_NAME);
                byte[] strToBytes = resultJson.getBytes();
                Files.write(path, strToBytes);

                getLog().info("Module Descriptor written on: " + path.toString());
            }

        } catch (Exception e) {
            String errorMessage = format("Could not build components definitions: %s", e.getMessage());
            throw new MojoFailureException(errorMessage, e);
        }
    }
}