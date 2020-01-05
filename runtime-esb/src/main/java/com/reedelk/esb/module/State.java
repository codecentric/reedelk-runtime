package com.reedelk.esb.module;

import com.reedelk.esb.flow.Flow;
import com.reedelk.esb.module.state.ModuleState;

import java.util.Collection;

public interface State {

    Collection<Flow> flows();

    Collection<Exception> errors();

    Collection<String> resolvedComponents();

    Collection<String> unresolvedComponents();

    ModuleState state();

}
