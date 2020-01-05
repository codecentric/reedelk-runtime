package com.reedelk.esb.services.module;

import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.commons.ModuleUtils;
import com.reedelk.runtime.system.api.ModuleService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Optional;

import static com.reedelk.esb.commons.Messages.Module.INSTALL_FAILED_MODULE_NAME_NOT_FOUND;
import static com.reedelk.esb.commons.Messages.Module.INSTALL_MODULE_DIFFERENT_VERSION_PRESENT;
import static java.util.Arrays.stream;

class SyncModuleService {

    private static final String UNKNOWN_VERSION = "UNKNOWN_VERSION";
    private static final Logger logger = LoggerFactory.getLogger(SyncModuleService.class);

    private final BundleContext context;
    private final ModuleService moduleService;

    public SyncModuleService(ModuleService moduleService, BundleContext context) {
        this.moduleService = moduleService;
        this.context = context;
    }

    /**
     * Checks whether the given module jar has a module name equal to a module already
     * installed in the runtime. If a module exists in the runtime with the same name,
     * it is then un-installed.
     * @param moduleJarPath the jar path of the module to test.
     */
    void unInstallIfModuleExistsAlready(String moduleJarPath) {
        String filePath = URI.create(moduleJarPath).getPath();
        String toBeInstalledModuleName = moduleNameOf(filePath).orElseThrow(() -> {
            String errorMessage = INSTALL_FAILED_MODULE_NAME_NOT_FOUND.format(moduleJarPath);
            return new ESBException(errorMessage);
        });


        findInstalledBundleMatchingModuleName(toBeInstalledModuleName).ifPresent(installedBundle -> {
            // The installed module has a different version, otherwise it would be just an update
            // and 'unInstallIfModuleExistsAlready' method would never be called.
            if (logger.isInfoEnabled()) {
                String message = INSTALL_MODULE_DIFFERENT_VERSION_PRESENT.format(
                        toBeInstalledModuleName,
                        installedBundle.getVersion(),
                        ModuleUtils.getModuleVersion(filePath).orElse(UNKNOWN_VERSION));
                logger.info(message);
            }

            // Exists a module with the same module name of the module we want to install.
            // We must uninstall the currently installed bundle. If the bundle belongs to the
            // runtime's modules directory, we also must remove it so that it will not be installed
            // again the next time the system restarts.
            String toBeUninstalled = installedBundle.getLocation();
            moduleService.uninstall(toBeUninstalled);
        });
    }

    private Optional<Bundle> findInstalledBundleMatchingModuleName(String targetModuleName) {
        return stream(context.getBundles())
                .filter(installedBundle -> installedBundle.getSymbolicName().equals(targetModuleName))
                .findFirst();
    }

    Optional<String> moduleNameOf(String moduleJarPath) {
        return ModuleUtils.getModuleName(moduleJarPath);
    }
}
