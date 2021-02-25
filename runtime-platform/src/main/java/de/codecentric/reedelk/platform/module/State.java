package de.codecentric.reedelk.platform.module;

import de.codecentric.reedelk.platform.module.state.ModuleState;
import de.codecentric.reedelk.platform.flow.Flow;

import java.util.Collection;

public interface State {

    Collection<Flow> flows();

    Collection<Exception> errors();

    Collection<String> resolvedComponents();

    Collection<String> unresolvedComponents();

    ModuleState state();

}
