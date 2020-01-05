package com.reedelk.runtime.api.script;

import com.reedelk.runtime.api.commons.ModuleContext;

public interface ScriptBlock {

    boolean isEmpty();

    String body();

    String functionName();

    ModuleContext getContext();

}
