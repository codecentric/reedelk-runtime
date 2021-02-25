package de.codecentric.reedelk.platform.module.state;

import de.codecentric.reedelk.platform.flow.Flow;
import de.codecentric.reedelk.platform.module.State;

import java.util.Collection;

abstract class AbstractState implements State {

    @Override
    public Collection<Flow> flows() {
        throw new UnsupportedOperationException("flows");
    }

    @Override
    public Collection<Exception> errors() {
        throw new UnsupportedOperationException("errors");
    }

    @Override
    public Collection<String> resolvedComponents() {
        throw new UnsupportedOperationException("resolvedComponents");
    }

    @Override
    public Collection<String> unresolvedComponents() {
        throw new UnsupportedOperationException("unresolvedComponents");
    }
}
