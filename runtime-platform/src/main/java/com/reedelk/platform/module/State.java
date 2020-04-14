package com.reedelk.platform.module;

import com.reedelk.platform.flow.Flow;
import com.reedelk.platform.module.state.ModuleState;

import java.util.Collection;

public interface State {

    Collection<Flow> flows();

    Collection<Exception> errors();

    Collection<String> resolvedComponents();

    Collection<String> unresolvedComponents();

    ModuleState state();

}
