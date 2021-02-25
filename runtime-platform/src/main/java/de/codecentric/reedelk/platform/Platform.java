package de.codecentric.reedelk.platform;

import de.codecentric.reedelk.platform.commons.Log;
import de.codecentric.reedelk.platform.configuration.ApplyRuntimeConfiguration;
import de.codecentric.reedelk.platform.lifecycle.*;
import de.codecentric.reedelk.platform.module.ModulesManager;
import de.codecentric.reedelk.platform.pubsub.Event;
import de.codecentric.reedelk.platform.services.ServicesManager;
import de.codecentric.reedelk.platform.services.hotswap.HotSwapListener;
import de.codecentric.reedelk.platform.services.module.EventListener;
import de.codecentric.reedelk.platform.services.module.EventService;
import de.codecentric.reedelk.platform.component.ComponentRegistry;
import de.codecentric.reedelk.platform.component.RuntimeComponents;
import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;
import de.codecentric.reedelk.runtime.system.api.SystemProperty;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.osgi.service.component.annotations.ServiceScope.SINGLETON;

@Component(service = Platform.class, scope = SINGLETON, immediate = true)
public class Platform implements EventListener, HotSwapListener {

    private static final Logger logger = LoggerFactory.getLogger(Platform.class);

    @Reference
    private ConfigurationAdmin configurationAdmin;

    protected BundleContext context;
    protected ModulesManager modulesManager;

    private EventService eventDispatcher;
    private ServicesManager servicesManager;
    private ComponentRegistry componentRegistry;

    @Activate
    public void start(BundleContext context) {
        this.context = context;

        modulesManager = new ModulesManager();

        componentRegistry = new ComponentRegistry(RuntimeComponents.allNames());
        eventDispatcher = new EventService(this);

        context.addBundleListener(eventDispatcher);
        context.addServiceListener(eventDispatcher);

        servicesManager = new ServicesManager(Platform.this, Platform.this, modulesManager, configurationAdmin);
        servicesManager.registerServices(context);

        ApplyRuntimeConfiguration.from(servicesManager.configurationService());
    }

    @Deactivate
    public void stop(BundleContext context) {
        context.removeBundleListener(eventDispatcher);
        context.removeServiceListener(eventDispatcher);
        servicesManager.unregisterServices();
    }

    @Override
    public synchronized void moduleInstalled(long moduleId) {
        StepRunner.get(context, modulesManager, componentRegistry)
                .next(new ModuleCreate())
                .next(new ModuleAdd())
                .execute(moduleId);
    }

    /**
     * There are two scenarios when this event callback is being called:
     * when a module is INSTALLED and when a module is UPDATED.
     *
     * Install:
     * - moduleInstalled: state=INSTALLED
     * - moduleStarted: state=STARTED
     *
     * Update (e.g. because component source code was changed - no hot-swap -):
     * - moduleStopping: state=RESOLVED
     * - componentUnregistering: state=UNRESOLVED
     * - moduleStopped: state=UNRESOLVED (no-op)
     * - componentRegistered: state=STARTED (auto-start when all components are resolved)
     * - moduleStarted: state=STARTED (no-op)
     */
    @Override
    public synchronized void moduleStarted(long moduleId) {
        ConfigurationService configurationService = servicesManager.configurationService();
        SystemProperty systemPropertyService = servicesManager.systemPropertyService();
        StepRunner.get(context, modulesManager, componentRegistry, configurationService, systemPropertyService)
                .next(new ModuleCheckNotNull())
                .next(new ModuleValidate())
                .next(new ModuleResolveDependencies())
                .next(new ModuleBuild())
                .next(new ModuleStart())
                .execute(moduleId);

        // After a module has been started, look-up for 'unresolved' modules. Resolve their
        // dependencies and check whether each 'unresolved' module can now be started:
        // the newly started module above might have just resolved some dependencies of the
        // unresolved modules.
        modulesManager.findUnresolvedModules().forEach(unresolvedModule ->
                StepRunner.get(context, modulesManager, componentRegistry, configurationService, systemPropertyService)
                        .next(new ModuleCheckNotNull())
                        .next(new ModuleUpdateRegisteredComponents())
                        .next(new ModuleBuild())
                        .next(new ModuleStart())
                        .execute(unresolvedModule.id()));
    }

    @Override
    public synchronized void moduleStopping(long moduleId) {
        StepRunner.get(context, modulesManager)
                .next(new ModuleCheckNotNull())
                .next(new ModuleStopAndReleaseReferences())
                .next(new ModuleTransitionToInstalled())
                .execute(moduleId);
    }

    /**
     * When the OSGi container process is stopped, 'moduleStopping' is not called therefore the module is
     * still registered in the ModuleManager. 'moduleStopped' is called when the OSGi container shuts down
     * (skipping the call to moduleStopping). Note that when a module is stopped there is no more context
     * (Bundle Context) associated with it.
     */
    @Override
    public synchronized void moduleStopped(long moduleId) {
        if (modulesManager.isModuleStarted(moduleId)) {
            StepRunner.get(context, modulesManager)
                    .next(new ModuleCheckNotNull())
                    .next(new ModuleStopAndReleaseReferences())
                    .next(new ModuleTransitionToInstalled())
                    .execute(moduleId);
        }
        // Important: module uninstalled event *must* be fired even if a module
        // has not been started. This is because there might be modules providing
        // only components and/or Script Functions without any flow and subscribers
        // of this event must be notified to property unregister script functions.
        Event.fireModuleUninstalled(moduleId);
    }

    @Override
    public synchronized void moduleUninstalled(long moduleId) {
        if (modulesManager.isModuleRegistered(moduleId)) {
            StepRunner.get(context, modulesManager)
                    .next(new ModuleRemove())
                    .execute(moduleId);
        }
        // Important: module uninstalled event *must* be fired even if a module
        // has not been started. This is because there might be modules providing
        // only components and/or Script Functions without any flow and subscribers
        // of this event must be notified to property unregister script functions.
        Event.fireModuleUninstalled(moduleId);
    }

    @Override
    public synchronized void componentRegistered(String componentName) {
        // We must update the registry to keep track of registered components whenever
        // a module is being installed. This event is called by the OSGi framework to
        // notify that a new service (i.e a new implementor) has been added.
        componentRegistry.registerComponent(componentName);
        Log.componentRegistered(logger, componentName);
    }

    @Override
    public synchronized void componentUnregistering(String componentName) {
        componentRegistry.unregisterComponent(componentName);
        Log.componentUnRegistered(logger, componentName);

        modulesManager.findModulesUsingComponent(componentName).forEach(moduleUsingComponent ->
                StepRunner.get(context, modulesManager)
                        .next(new ModuleCheckNotNull())
                        .next(new ModuleStopAndReleaseReferences())
                        .next(new ModuleUpdateUnregisteredComponent(componentName))
                        .execute(moduleUsingComponent.id()));
    }

    @Override
    public synchronized void hotSwap(long moduleId, String resourcesRootDirectory) {
        ConfigurationService configurationService = servicesManager.configurationService();
        SystemProperty systemPropertyService = servicesManager.systemPropertyService();
        StepRunner.get(context, modulesManager, componentRegistry, configurationService, systemPropertyService)
                .next(new ModuleCheckNotNull())
                .next(new ModuleStopAndReleaseReferences())
                .next(new ModuleRemove())
                .next(new ModuleHotSwap(resourcesRootDirectory))
                .next(new ModuleAdd())
                .next(new ModuleValidate())
                .next(new ModuleResolveDependencies())
                .next(new ModuleBuild())
                .next(new ModuleStart())
                .execute(moduleId);
    }
}
