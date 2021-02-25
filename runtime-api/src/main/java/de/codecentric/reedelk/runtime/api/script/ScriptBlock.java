package de.codecentric.reedelk.runtime.api.script;

import de.codecentric.reedelk.runtime.api.commons.ModuleContext;

public interface ScriptBlock {

    boolean isEmpty();

    String body();

    String functionName();

    ModuleContext getContext();

}
