package com.reedelk.runtime.commons;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ModuleUtils {

    private ModuleUtils() {
    }

    public static boolean isModule(File jarFile) {
        return getAttributeValue(jarFile, ModuleProperties.Bundle.MODULE_HEADER_NAME)
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    public static Optional<String> getModuleVersion(String jarFilePath) {
        return getAttributeValue(jarFilePath, ModuleProperties.Bundle.MODULE_VERSION);
    }

    public static Optional<String> getModuleName(String jarFilePath) {
        return getAttributeValue(jarFilePath, ModuleProperties.Bundle.MODULE_SYMBOLIC_NAME);
    }

    private static Optional<String> getAttributeValue(String jarFilePath, String attributeKey) {
        return getAttributeValue(new File(jarFilePath), attributeKey);
    }

    private static Optional<String> getAttributeValue(File jarFile, String attributeKey) {
        try {
            Attributes attributes = getManifestAttributesOf(jarFile);
            return Optional.ofNullable(attributes.getValue(attributeKey));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    private static Attributes getManifestAttributesOf(File file) throws IOException {
        try (JarFile jarFile = new JarFile(file)) {
            Manifest manifest = jarFile.getManifest();
            return manifest.getMainAttributes();
        }
    }
}
