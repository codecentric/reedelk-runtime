package de.codecentric.reedelk.platform.module.state;

import java.util.ArrayList;
import java.util.Collection;

public class Error extends AbstractState {

    private final Collection<Exception> exceptions = new ArrayList<>();
    private final Collection<String> resolvedComponents = new ArrayList<>();

    public Error(Collection<Exception> exceptions, Collection<String> resolvedComponents) {
        this.exceptions.addAll(exceptions);
        this.resolvedComponents.addAll(resolvedComponents);
    }

    @Override
    public ModuleState state() {
        return ModuleState.ERROR;
    }

    @Override
    public Collection<Exception> errors() {
        return exceptions;
    }

    @Override
    public Collection<String> resolvedComponents() {
        return resolvedComponents;
    }
}
