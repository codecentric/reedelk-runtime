package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.ModulesManager;
import de.codecentric.reedelk.platform.component.ComponentRegistry;
import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;
import de.codecentric.reedelk.runtime.system.api.SystemProperty;
import de.codecentric.reedelk.runtime.api.commons.Preconditions;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.List;

import static de.codecentric.reedelk.runtime.api.commons.Preconditions.checkArgument;

public class StepRunner {

    private List<Step<?, ?>> steps = new ArrayList<>();

    private final BundleContext context;
    private final ModulesManager modulesManager;
    private final ComponentRegistry componentRegistry;
    private final SystemProperty systemPropertyService;
    private final ConfigurationService configurationService;

    private StepRunner(BundleContext context, ModulesManager modulesManager, ComponentRegistry componentRegistry, ConfigurationService configurationService, SystemProperty systemPropertyService) {
        this.context = context;
        this.modulesManager = modulesManager;
        this.componentRegistry = componentRegistry;
        this.configurationService = configurationService;
        this.systemPropertyService = systemPropertyService;
    }

    private StepRunner(BundleContext context, ModulesManager modulesManager, ComponentRegistry componentRegistry) {
        this(context, modulesManager, componentRegistry, null, null);
    }

    public static StepRunner get(BundleContext context, ModulesManager modulesManager, ComponentRegistry componentRegistry, ConfigurationService configurationService, SystemProperty systemPropertyService) {
        return new StepRunner(context, modulesManager, componentRegistry, configurationService, systemPropertyService);
    }

    public static StepRunner get(BundleContext context, ModulesManager modulesManager, ConfigurationService configurationService) {
        return new StepRunner(context, modulesManager, null, configurationService, null);
    }

    public static StepRunner get(BundleContext context, ModulesManager modulesManager, ComponentRegistry componentRegistry) {
        return new StepRunner(context, modulesManager, componentRegistry);
    }

    public static StepRunner get(BundleContext context, ModulesManager modulesManager) {
        return new StepRunner(context, modulesManager, null);
    }

    public static StepRunner get(BundleContext context) {
        return new StepRunner(context, null, null);
    }

    public StepRunner next(Step<?, ?> stepToAdd) {
        Preconditions.checkArgument(stepToAdd != null, "added step was null.");
        this.steps.add(stepToAdd);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(long moduleId) {
        // When we install a module, we don't have the Module
        // already registered in the manager, therefore the module
        // manager is not needed (e.g ModuleResolveDependencies).
        // In all the other cases, by default, the first step must
        // accept in input the module to be processed.
        Object output = modulesManager.getModuleById(moduleId);
        Bundle bundle = context.getBundle(moduleId);

        for (Step step : steps) {
            step.bundle(bundle);
            step.modulesManager(modulesManager);
            step.componentRegistry(componentRegistry);
            step.configurationService(configurationService);
            step.systemPropertyService(systemPropertyService);

            output = step.run(output);
        }
    }
}
