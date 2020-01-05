package com.reedelk.esb.module.state;

import com.reedelk.esb.flow.Flow;

import java.util.Collection;

public class Started extends AbstractState {

    private final Collection<Flow> flows;
    private final Collection<String> resolvedComponents;

    public Started(Collection<Flow> flows, Collection<String> resolvedComponents) {
        this.flows = flows;
        this.resolvedComponents = resolvedComponents;
    }

    @Override
    public Collection<Flow> flows() {
        return flows;
    }

    @Override
    public Collection<String> resolvedComponents() {
        return resolvedComponents;
    }

    @Override
    public ModuleState state() {
        return ModuleState.STARTED;
    }

}
