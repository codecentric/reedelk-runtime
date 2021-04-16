package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.Module;

import java.util.ArrayList;
import java.util.Collection;

import static de.codecentric.reedelk.platform.module.state.ModuleState.UNRESOLVED;
import static de.codecentric.reedelk.runtime.api.commons.Preconditions.checkState;

public class ModuleUpdateRegisteredComponents extends AbstractStep<Module, Module> {

    @Override
    public Module run(Module module) {

        checkState(module.state() == UNRESOLVED,
                String.format("Module state was=%s. Only state UNRESOLVED allowed", module.state()));

        // Create a collection with all the components (resolved and not resolved)
        // belonging to the current module.
        Collection<String> allComponents = new ArrayList<>(module.resolvedComponents());
        allComponents.addAll(module.unresolvedComponents());

        // Extract the unresolved components by asking the registry which amongst
        // all the components are unresolved.
        Collection<String> unresolvedComponents = componentRegistry().unregisteredComponentsOf(allComponents);

        // If there are NOT unresolved components, then we can transition the module
        // state to RESOLVED, otherwise the component stays in the UNRESOLVED state
        // because not all the dependencies could be resolved this time.
        if (unresolvedComponents.isEmpty()) {
            module.resolve(allComponents);
        } else {
            // We remove the unresolved components from all the components:
            // Therefore the collection 'allComponents' contains only the resolved ones.
            allComponents.removeAll(unresolvedComponents);
            module.unresolve(unresolvedComponents, allComponents);
        }

        return module;
    }
}
