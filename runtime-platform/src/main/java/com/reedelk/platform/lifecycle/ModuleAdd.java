package com.reedelk.platform.lifecycle;

import com.reedelk.platform.module.Module;

public class ModuleAdd extends AbstractStep<Module, Module> {

    @Override
    public Module run(Module module) {
        modulesManager().add(module);
        return module;
    }
}
