package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.platform.module.state.ModuleState;

public class ModuleTransitionToInstalled extends AbstractStep<Module, Module> {

    @Override
    public Module run(Module module) {

        // Nothing to do if its state is already installed.
        if (module.state() == ModuleState.INSTALLED) return module;

        module.installed();
        return module;
    }
}
