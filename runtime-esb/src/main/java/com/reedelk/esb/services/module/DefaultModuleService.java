package com.reedelk.esb.services.module;

import com.reedelk.esb.module.ModulesManager;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.system.api.ModuleDto;
import com.reedelk.runtime.system.api.ModuleService;
import com.reedelk.runtime.system.api.ModulesDto;
import com.reedelk.runtime.system.api.SystemProperty;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static com.reedelk.esb.commons.FunctionWrapper.uncheckedConsumer;
import static com.reedelk.esb.commons.Messages.Module.*;
import static com.reedelk.runtime.api.commons.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

public class DefaultModuleService implements ModuleService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultModuleService.class);
    private static final long NOTHING_UNINSTALLED_MODULE_ID = -1L;

    private final ModulesMapper mapper;
    private final EventListener listener;
    private final BundleContext context;
    private final ModulesManager modulesManager;
    private final SyncModuleService syncModuleService;
    private final SystemProperty systemProperty;

    public DefaultModuleService(BundleContext context,
                                ModulesManager modulesManager,
                                SystemProperty systemProperty,
                                EventListener listener) {
        this.modulesManager = modulesManager;
        this.listener = listener;
        this.context = context;
        this.systemProperty = systemProperty;
        this.mapper = new ModulesMapper();
        this.syncModuleService = new SyncModuleService(this, context);
    }

    @Override
    public long install(String moduleJarPath) {
        if (getModuleAtPath(moduleJarPath).isPresent()) {
            String errorMessage = INSTALL_FAILED_MODULE_ALREADY_INSTALLED.format(moduleJarPath);
            throw new IllegalStateException(errorMessage);
        }

        // This is to un-install a module with the same name of the one we are going to install,
        // but with a different module jar path or a different version.
        unInstallIfModuleExistsAlready(moduleJarPath);

        try {
            Bundle installedBundle = context.installBundle(moduleJarPath);
            if (logger.isInfoEnabled()) {
                logger.info(INSTALL_SUCCESS.format(installedBundle.getSymbolicName()));
            }
            return start(installedBundle);
        } catch (BundleException e) {
            String errorMessage = INSTALL_FAILED.format(moduleJarPath);
            throw new ESBException(errorMessage, e);
        }
    }

    @Override
    public long update(String moduleJarPath) {
        // IMPORTANT: On Java 8 this does not compile if we collapse the lambda.
        Bundle bundleAtPath = getModuleAtPath(moduleJarPath).orElseThrow(new Supplier<IllegalStateException>() {
            @Override
            public IllegalStateException get() {
                String errorMessage = UPDATE_FAILED_MODULE_NOT_FOUND.format(moduleJarPath);
                throw new IllegalStateException(errorMessage);
            }
        });

        if (Bundle.INSTALLED == bundleAtPath.getState()) {
            // It is installed but not started (we don't have to call listener's moduleStopping event)
            executeOperation(bundleAtPath, Bundle::update, Bundle::start);
        } else {
            // It is installed and started (we must stop it, update it and start it again)
            listener.moduleStopping(bundleAtPath.getBundleId());
            executeOperation(bundleAtPath, Bundle::stop, Bundle::update, Bundle::start);
        }

        if (logger.isInfoEnabled()) {
            logger.info(UPDATE_SUCCESS.format(bundleAtPath.getSymbolicName()));
        }

        return bundleAtPath.getBundleId();
    }

    @Override
    public long installOrUpdate(String moduleJarPath) {
        return getModuleAtPath(moduleJarPath)
                .map(bundle -> update(moduleJarPath))
                .orElseGet(() -> install(moduleJarPath));
    }

    @Override
    public long uninstall(String moduleJarPath) {
        return getModuleAtPath(moduleJarPath).map(bundleAtPath -> {
            listener.moduleStopping(bundleAtPath.getBundleId());
            executeOperation(bundleAtPath, Bundle::stop, Bundle::uninstall, deleteModuleBundleJarOperation());
            if (logger.isInfoEnabled()) {
                logger.info(UNINSTALL_SUCCESS.format(bundleAtPath.getSymbolicName()));
            }
            return bundleAtPath.getBundleId();
        }).orElse(NOTHING_UNINSTALLED_MODULE_ID);
    }

    @Override
    public ModulesDto modules() {
        Set<ModuleDto> moduleDTOs = modulesManager.allModules()
                .stream()
                .map(mapper::map)
                .collect(toSet());
        ModulesDto modulesDto = new ModulesDto();
        modulesDto.setModuleDtos(moduleDTOs);
        return modulesDto;
    }

    void unInstallIfModuleExistsAlready(String moduleJarPath) {
        // If the module to be installed at the given module jar path has a symbolic name of an already
        // installed module, then we must uninstall it from the runtime before installing the new one.
        // This is needed to prevent having in the runtime two modules with exactly the same components
        // but with different versions.
        syncModuleService.unInstallIfModuleExistsAlready(moduleJarPath);
    }

    Operation deleteModuleBundleJarOperation() {
        return new DeleteModuleBundleJar(systemProperty);
    }

    private long start(Bundle installedBundle) {
        checkNotNull(installedBundle, "installedBundle");
        try {
            installedBundle.start();
            if (logger.isInfoEnabled()) {
                logger.info(START_SUCCESS.format(installedBundle.getSymbolicName()));
            }
            return installedBundle.getBundleId();
        } catch (BundleException exception) {
            String errorMessage = START_FAILED.format(installedBundle.getSymbolicName());
            throw new ESBException(errorMessage, exception);
        }
    }

    private Optional<Bundle> getModuleAtPath(String bundlePath) {
        return Optional.ofNullable(context.getBundle(bundlePath));
    }

    interface Operation {
        void execute(Bundle bundle) throws BundleException;
    }

    private void executeOperation(Bundle bundle, Operation... operations) {
        stream(operations).forEachOrdered(
                uncheckedConsumer(operation -> operation.execute(bundle)));

    }

    /**
     * Removes a Module Bundle Jar file if and only if it belongs to the modules directory.
     */
    static class DeleteModuleBundleJar implements Operation {

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
}
