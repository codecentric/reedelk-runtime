package de.codecentric.reedelk.runtime;

import de.codecentric.reedelk.runtime.commons.FileExtension;
import de.codecentric.reedelk.runtime.commons.RuntimeMessage;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.stream;

class ModuleInstaller {

    private final Logger logger = LoggerFactory.getLogger(ModuleInstaller.class);

    private final Application application;

    private ModuleInstaller(final Application application) {
        this.application = application;
    }

    static void install(Application application, String autoDeployDir) {
        new ModuleInstaller(application).deployAll(autoDeployDir);
    }

    private void deployAll(String autoDeployDir) {
        // Look in the specified bundle directory to create a list
        // of all JAR files to install.
        File[] files = new File(autoDeployDir).listFiles();
        if (files == null) return;

        Set<String> installedBundleNames = new HashSet<>();
        List<Bundle> toStart = new ArrayList<>();
        stream(files)
                .filter(FileExtension.JAR::is)
                .forEach(file -> application.install(file).ifPresent(installedBundle -> {

                    // We must make sure that only one bundle with a given symbolic name is installed.
                    // If there are multiple bundles with the same symbolic name but with different versions in the
                    // auto-deploy directory, then we just install only one of them.
                    String installedBundleSymbolicName = installedBundle.getSymbolicName();
                    if (installedBundleNames.contains(installedBundleSymbolicName)) {
                        try {

                            String warnMessage = RuntimeMessage.message("module.installer.warn.multiple.modules", installedBundleSymbolicName);
                            logger.warn(warnMessage);

                            installedBundle.uninstall();

                        } catch (BundleException exception) {
                            // We stop if we cannot un-install.
                            String errorMessage = RuntimeMessage.message("module.installer.duplicated.module.uninstall.error", exception.getMessage());
                            logger.error(errorMessage);
                            application.stop();
                        }

                    } else {
                        // We add the current installed module name to the list of already installed modules.
                        installedBundleNames.add(installedBundleSymbolicName);
                        toStart.add(installedBundle);
                    }
                }));

        toStart.forEach(application::start);
    }
}
