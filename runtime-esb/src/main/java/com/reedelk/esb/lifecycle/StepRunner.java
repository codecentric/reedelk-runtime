package com.reedelk.esb.lifecycle;

import com.reedelk.esb.component.ComponentRegistry;
import com.reedelk.esb.module.ModulesManager;
import com.reedelk.runtime.api.configuration.ConfigurationService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.List;

import static com.reedelk.runtime.api.commons.Preconditions.checkArgument;

public class StepRunner {

    private List<Step<?, ?>> steps = new ArrayList<>();

    private final BundleContext context;
    private final ModulesManager modulesManager;
    private final ComponentRegistry componentRegistry;
    private final ConfigurationService configurationService;

    private StepRunner(BundleContext context, ModulesManager modulesManager, ComponentRegistry componentRegistry, ConfigurationService configurationService) {
        this.context = context;
        this.modulesManager = modulesManager;
        this.componentRegistry = componentRegistry;
        this.configurationService = configurationService;
    }

    private StepRunner(BundleContext context, ModulesManager modulesManager, ComponentRegistry componentRegistry) {
        this(context, modulesManager, componentRegistry, null);
    }

    public static StepRunner get(BundleContext context, ModulesManager modulesManager, ComponentRegistry componentRegistry, ConfigurationService configurationService) {
        return new StepRunner(context, modulesManager, componentRegistry, configurationService);
    }

    public static StepRunner get(BundleContext context, ModulesManager modulesManager, ConfigurationService configurationService) {
        return new StepRunner(context, modulesManager, null, configurationService);
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

    public StepRunner next(Step stepToAdd) {
        checkArgument(stepToAdd != null, "added step was null.");
        this.steps.add(stepToAdd);
        return this;
    }

    @SuppressWarnings("unchecked")
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

            output = step.run(output);
        }
    }
}
