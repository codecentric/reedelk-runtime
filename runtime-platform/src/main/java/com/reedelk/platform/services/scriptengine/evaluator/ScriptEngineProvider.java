package com.reedelk.platform.services.scriptengine.evaluator;

import javax.script.ScriptException;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;

public interface ScriptEngineProvider {

    void compile(String functionDefinition) throws ScriptException;

    void compile(Collection<String> modules, Reader reader, Map<String, Object> bindings) throws ScriptException;

    Object invokeFunction(String functionName, Object ...args) throws NoSuchMethodException, ScriptException;

    void bind(Map<String, Object> bindingKeysAndValues);

    void removeBinding(String bindingKey);

}
