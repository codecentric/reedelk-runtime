package com.reedelk.platform.module.state;

import com.reedelk.platform.flow.Flow;

import java.util.Collection;

public class Stopped extends AbstractState {

    private final Collection<Flow> flows;
    private final Collection<String> resolvedComponents;

    public Stopped(Collection<Flow> flows, Collection<String> resolvedComponents) {
        this.resolvedComponents = resolvedComponents;
        this.flows = flows;
    }

    @Override
    public Collection<String> resolvedComponents() {
        return resolvedComponents;
    }

    @Override
    public Collection<Flow> flows() {
        return flows;
    }

    @Override
    public ModuleState state() {
        return ModuleState.STOPPED;
    }
}
