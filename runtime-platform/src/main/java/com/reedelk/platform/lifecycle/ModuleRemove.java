package com.reedelk.platform.lifecycle;

import com.reedelk.platform.module.Module;
import com.reedelk.platform.module.state.ModuleState;

import static com.reedelk.runtime.api.commons.Preconditions.checkState;

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
