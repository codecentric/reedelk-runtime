package de.codecentric.reedelk.platform.services.scriptengine.evaluator.function;

import de.codecentric.reedelk.runtime.api.script.ScriptBlock;

import java.util.function.Function;

public interface FunctionDefinitionBuilder<T extends ScriptBlock> extends Function<T, String> {

}
