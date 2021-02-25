package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.Module;

public class ModuleAdd extends AbstractStep<Module, Module> {

    @Override
    public Module run(Module module) {
        modulesManager().add(module);
        return module;
    }
}
