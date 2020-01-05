package com.reedelk.esb.lifecycle;

import com.reedelk.esb.module.Module;

public class ModuleAdd extends AbstractStep<Module, Module> {

    @Override
    public Module run(Module module) {
        modulesManager().add(module);
        return module;
    }
}
