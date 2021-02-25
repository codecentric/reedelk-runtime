package de.codecentric.reedelk.platform.services;

import de.codecentric.reedelk.platform.services.configuration.DefaultConfigurationService;
import de.codecentric.reedelk.platform.services.configuration.DefaultSystemPropertyService;
import de.codecentric.reedelk.platform.services.converter.DefaultConverterService;
import de.codecentric.reedelk.platform.services.hotswap.DefaultHotSwapService;
import de.codecentric.reedelk.platform.services.hotswap.HotSwapListener;
import de.codecentric.reedelk.platform.services.module.DefaultModuleService;
import de.codecentric.reedelk.platform.services.module.EventListener;
import de.codecentric.reedelk.platform.services.resource.DefaultResourceService;
import de.codecentric.reedelk.platform.services.scriptengine.ScriptEngine;
import de.codecentric.reedelk.platform.module.ModulesManager;
import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;
import de.codecentric.reedelk.runtime.api.converter.ConverterService;
import de.codecentric.reedelk.runtime.api.resource.ResourceService;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import de.codecentric.reedelk.runtime.system.api.SystemProperty;
import de.codecentric.reedelk.runtime.system.api.HotSwapService;
import de.codecentric.reedelk.runtime.system.api.ModuleService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Properties;

public class ServicesManager {

    private static final Dictionary NO_PROPERTIES = new Properties();

    private final EventListener eventListener;
    private final ModulesManager modulesManager;
    private final HotSwapListener hotSwapListener;
    private final ConfigurationAdmin configurationAdmin;

    private List<ServiceRegistration<?>> registeredServices = new ArrayList<>();

    private DefaultConfigurationService configurationService;
    private DefaultSystemPropertyService systemProperty;

    public ServicesManager(EventListener eventListener,
                           HotSwapListener hotSwapListener,
                           ModulesManager modulesManager,
                           ConfigurationAdmin configurationAdmin) {
        this.eventListener = eventListener;
        this.modulesManager = modulesManager;
        this.hotSwapListener = hotSwapListener;
        this.configurationAdmin = configurationAdmin;
    }

    public void registerServices(BundleContext context) {
        registerSystemPropertyService(context);
        registerModuleService(context);
        registerHotSwapService(context);
        registerConverterService(context);
        registerScriptEngineService(context);
        registerConfigurationService(context);
        registerModuleFileProviderService(context);
    }

    public void unregisterServices() {
        registeredServices.forEach(ServiceRegistration::unregister);
    }

    public ConfigurationService configurationService() {
        return configurationService;
    }

    public SystemProperty systemPropertyService() {
        return systemProperty;
    }

    private void registerSystemPropertyService(BundleContext context) {
        systemProperty = new DefaultSystemPropertyService(context);
        ServiceRegistration<SystemProperty> registration =
                registerService(context, SystemProperty.class, systemProperty);
        registeredServices.add(registration);
    }

    private void registerConfigurationService(BundleContext context) {
        configurationService = new DefaultConfigurationService(configurationAdmin, systemProperty);
        configurationService.initialize();
        ServiceRegistration<ConfigurationService> registration =
                registerService(context, ConfigurationService.class, configurationService);
        registeredServices.add(registration);
    }

    private void registerScriptEngineService(BundleContext context) {
        ScriptEngine scriptEngineService = ScriptEngine.getInstance();
        ServiceRegistration<ScriptEngineService> registration =
                registerService(context, ScriptEngineService.class, scriptEngineService);
        registeredServices.add(registration);
    }

    private void registerHotSwapService(BundleContext context) {
        DefaultHotSwapService service = new DefaultHotSwapService(context, hotSwapListener);
        ServiceRegistration<HotSwapService> registration =
                registerService(context, HotSwapService.class, service);
        registeredServices.add(registration);
    }

    private void registerModuleService(BundleContext context) {
        DefaultModuleService service = new DefaultModuleService(context, modulesManager, systemProperty, eventListener);
        ServiceRegistration<ModuleService> registration =
                registerService(context, ModuleService.class, service);
        registeredServices.add(registration);
    }

    private void registerModuleFileProviderService(BundleContext context) {
        ScriptEngine scriptEngineService = ScriptEngine.getInstance();
        ResourceService service = new DefaultResourceService(scriptEngineService);
        ServiceRegistration<ResourceService> registration =
                registerService(context, ResourceService.class, service);
        registeredServices.add(registration);
    }

    private void registerConverterService(BundleContext context) {
        ConverterService service = DefaultConverterService.getInstance();
        ServiceRegistration<ConverterService> registration =
                registerService(context, ConverterService.class, service);
        registeredServices.add(registration);
    }

    @SuppressWarnings("unchecked")
    private <T> ServiceRegistration<T> registerService(BundleContext context, Class<T> serviceClazz, T serviceImplementation) {
        return context.registerService(serviceClazz, serviceImplementation, NO_PROPERTIES);
    }
}
