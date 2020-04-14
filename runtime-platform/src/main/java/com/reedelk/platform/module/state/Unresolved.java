package com.reedelk.platform.module.state;

import java.util.Collection;

public class Unresolved extends AbstractState {

    private final Collection<String> resolvedComponents;
    private final Collection<String> unresolvedComponents;

    public Unresolved(Collection<String> resolvedComponents, Collection<String> unresolvedComponents) {
        this.resolvedComponents = resolvedComponents;
        this.unresolvedComponents = unresolvedComponents;
    }

    @Override
    public Collection<String> resolvedComponents() {
        return resolvedComponents;
    }

    @Override
    public Collection<String> unresolvedComponents() {
        return unresolvedComponents;
    }

    @Override
    public ModuleState state() {
        return ModuleState.UNRESOLVED;
    }

}
