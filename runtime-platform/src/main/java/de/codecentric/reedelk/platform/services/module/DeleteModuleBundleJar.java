package de.codecentric.reedelk.platform.services.module;

import de.codecentric.reedelk.runtime.system.api.SystemProperty;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static de.codecentric.reedelk.platform.commons.Messages.Module.REMOVE_MODULE_FROM_DIRECTORY_ERROR;
import static de.codecentric.reedelk.platform.commons.Messages.Module.REMOVE_MODULE_FROM_DIRECTORY_EXCEPTION;

/**
 * Removes a Module Bundle Jar file if and only if it belongs to the modules directory.
 */
class DeleteModuleBundleJar implements DefaultModuleService.Operation {

    private static final Logger logger = LoggerFactory.getLogger(DeleteModuleBundleJar.class);

    private final SystemProperty systemProperty;

    DeleteModuleBundleJar(SystemProperty systemProperty) {
        this.systemProperty = systemProperty;
    }

    @Override
    public void execute(Bundle toDelete) {
        // The 'toDelete' bundle's  location is a URI, but we need the file path.
        URI uri = URI.create(toDelete.getLocation());
        String filePath = new File(uri.getPath()).getPath();

        // We remove the file if and only if it belongs to the modules directory.
        if (filePath.startsWith(systemProperty.modulesDirectory())) {
            try {
                boolean deleteSuccess = Files.deleteIfExists(Paths.get(uri));
                if (!deleteSuccess && logger.isWarnEnabled()) {
                    String message = REMOVE_MODULE_FROM_DIRECTORY_ERROR.format(
                            toDelete.getSymbolicName(),
                            toDelete.getVersion(),
                            filePath);
                    logger.error(message);
                }
            } catch (IOException exception) {
                if (logger.isWarnEnabled()) {
                    String message = REMOVE_MODULE_FROM_DIRECTORY_EXCEPTION.format(
                            toDelete.getSymbolicName(),
                            toDelete.getVersion(),
                            filePath,
                            exception.getMessage());
                    logger.error(message);
                }
            }
        }
    }
}
