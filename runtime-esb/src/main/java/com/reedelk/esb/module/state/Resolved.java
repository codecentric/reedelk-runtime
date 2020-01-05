package com.reedelk.esb.module.state;

import java.util.Collection;

public class Resolved extends AbstractState {

    private final Collection<String> resolvedComponents;

    public Resolved(Collection<String> resolvedComponents) {
        this.resolvedComponents = resolvedComponents;
    }

    @Override
    public Collection<String> resolvedComponents() {
        return resolvedComponents;
    }

    @Override
    public ModuleState state() {
        return ModuleState.RESOLVED;
    }

}
