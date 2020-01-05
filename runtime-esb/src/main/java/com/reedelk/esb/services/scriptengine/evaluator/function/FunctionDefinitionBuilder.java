package com.reedelk.esb.services.scriptengine.evaluator.function;

import com.reedelk.runtime.api.script.ScriptBlock;

public interface FunctionDefinitionBuilder<T extends ScriptBlock> {

    String from(T scriptBlock);

}
