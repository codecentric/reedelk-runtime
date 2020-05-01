package com.reedelk.module.descriptor.analyzer.commons;

import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.model.ModuleDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ReadModuleDescriptor {

    private ReadModuleDescriptor() {
    }

    public static Optional<String> from(String jarFilePath) {
        try (JarFile file = new JarFile(jarFilePath)) {
            ZipEntry componentsConfigFile = file.getEntry(ModuleDescriptor.RESOURCE_FILE_NAME);
            if (componentsConfigFile == null) return Optional.empty();

            String text;
            try (InputStream inputStream = file.getInputStream(componentsConfigFile);
                 Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                text = scanner.useDelimiter("\\A").next();
            }
            return Optional.ofNullable(text);
        } catch (IOException e) {
            throw new ModuleDescriptorException("Components definition from Jar: " + jarFilePath, e);
        }
    }
}
