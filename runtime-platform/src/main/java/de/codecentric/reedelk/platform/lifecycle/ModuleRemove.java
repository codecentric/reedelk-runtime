package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.platform.module.state.ModuleState;

import static de.codecentric.reedelk.runtime.api.commons.Preconditions.checkState;

public class ModuleRemove extends AbstractStep<Module, Void> {

    @Override
    public Void run(Module module) {
        checkState(module.state() != ModuleState.STARTED,
                "Module with id=[%d], name=[%s] could not be removed: its state is [%s]",
                module.id(),
                module.name(),
                module.state());
        modulesManager().removeModuleById(module.id());
        return NOTHING;
    }
}
