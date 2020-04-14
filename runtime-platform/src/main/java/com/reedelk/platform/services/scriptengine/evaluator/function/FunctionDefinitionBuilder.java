package com.reedelk.platform.services.scriptengine.evaluator.function;

import com.reedelk.runtime.api.script.ScriptBlock;

import java.util.function.Function;

public interface FunctionDefinitionBuilder<T extends ScriptBlock> extends Function<T, String> {

}
