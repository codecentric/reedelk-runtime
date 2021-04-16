package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.Module;
import org.osgi.framework.Bundle;

import static de.codecentric.reedelk.runtime.api.commons.Preconditions.checkState;

public class ModuleCheckNotNull extends AbstractStep<Module, Module> {

    @Override
    public Module run(Module module) {
        Bundle bundle = bundle();
        long moduleId = bundle.getBundleId();
        checkState(module != null,
                "Module with id=[%d], symbolic name=[%s] was not found in Module Manager",
                moduleId,
                bundle.getSymbolicName());
        return module;
    }
}
