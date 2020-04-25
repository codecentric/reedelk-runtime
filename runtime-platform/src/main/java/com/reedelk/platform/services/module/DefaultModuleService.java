package com.reedelk.platform.services.module;

import com.reedelk.platform.module.ModulesManager;
import com.reedelk.runtime.api.commons.Unchecked;
import com.reedelk.runtime.api.exception.PlatformException;
import com.reedelk.runtime.commons.ModuleUtils;
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
import java.net.URI;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static com.reedelk.platform.commons.Messages.Module.*;
import static com.reedelk.runtime.api.commons.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

public class DefaultModuleService implements ModuleService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultModuleService.class);
    private static final long NOTHING_UNINSTALLED_MODULE_ID = -1L;

    private final ModulesMapper mapper;
    private final BundleContext context;
    private final EventListener listener;
    private final ModulesManager modulesManager;
    private final SystemProperty systemProperty;
    private final SyncModuleService syncModuleService;

    public DefaultModuleService(BundleContext context,
                                ModulesManager modulesManager,
                                SystemProperty systemProperty,
                                EventListener listener) {
        this.context = context;
        this.listener = listener;
        this.modulesManager = modulesManager;
        this.systemProperty = systemProperty;
        this.mapper = new ModulesMapper();
        this.syncModuleService = new SyncModuleService(this, context);
    }

    @Override
    public long install(String moduleJarPath) {
        // If the module is already installed we are in an illegal state condition,
        // and therefore we throw an exception.
        checkIsModuleNotInstalledOrThrow(moduleJarPath);

        // Before moving on, we must make sure that the module being installed
        // is actually an valid module.
        checkIsValidModuleOrUnInstallAndThrow(moduleJarPath);

        // This is to un-install a module with the same name of the one we are going to install,
        // but with a different module jar path or a different version.
        unInstallIfModuleExistsAlready(moduleJarPath);

        try {
            Bundle installedBundle = context.installBundle(moduleJarPath);
            if (logger.isInfoEnabled()) {
                logger.info(INSTALL_SUCCESS.format(installedBundle.getSymbolicName()));
            }
            return start(installedBundle);
        } catch (BundleException exception) {
            String errorMessage = INSTALL_FAILED.format(moduleJarPath);
            throw new PlatformException(errorMessage, exception);
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

    Operation deleteModuleBundleJarOperation() {
        return new DeleteModuleBundleJar(systemProperty);
    }

    void unInstallIfModuleExistsAlready(String moduleJarPath) {
        // If the module to be installed at the given module jar path has a symbolic name of an already
        // installed module, then we must uninstall it from the runtime before installing the new one.
        // This is needed to prevent having in the runtime two modules with exactly the same components
        // but with different versions.
        syncModuleService.unInstallIfModuleExistsAlready(moduleJarPath);
    }

    // IMPORTANT: The module Jar path is a URI.
    void checkIsValidModuleOrUnInstallAndThrow(String moduleJarPath) {
        File moduleFile = Paths.get(URI.create(moduleJarPath)).toFile();
        if (!ModuleUtils.isModule(moduleFile)) {
            deleteFile(moduleFile);
            String errorMessage = INSTALL_FAILED_MODULE_NOT_VALID.format(moduleJarPath);
            throw new PlatformException(errorMessage);
        }
    }

    private void checkIsModuleNotInstalledOrThrow(String moduleJarPath) {
        if (getModuleAtPath(moduleJarPath).isPresent()) {
            String errorMessage = INSTALL_FAILED_MODULE_ALREADY_INSTALLED.format(moduleJarPath);
            throw new IllegalStateException(errorMessage);
        }
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
            throw new PlatformException(errorMessage, exception);
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
                Unchecked.consumer(operation -> operation.execute(bundle)));
    }

    private void deleteFile(File fileToDelete) {
        boolean deleteSuccessful = fileToDelete.delete();
        if (!deleteSuccessful && logger.isWarnEnabled()) {
            logger.warn(REMOVE_FILE_NOT_SUCCESSFUL.format(fileToDelete.getPath()));
        }
    }
}
