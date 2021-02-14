package de.codecentric.reedelk.platform.services.scriptengine;

import de.codecentric.reedelk.platform.services.scriptengine.evaluator.ScriptEngineProvider;

import javax.script.ScriptEngine;
import javax.script.*;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;

import static javax.script.ScriptContext.ENGINE_SCOPE;

public class GroovyEngineProvider implements ScriptEngineProvider {

    private final ScriptEngine engine;
    private final Invocable invocable;
    private final Compilable compilable;

    private GroovyEngineProvider() {
        ScriptEngineManager factory = new ScriptEngineManager();
        this.engine = factory.getEngineByName("groovy");
        this.invocable = (Invocable) engine;
        this.compilable = (Compilable) engine;
    }

    private static class ScriptEngineProviderHelper {
        private static final GroovyEngineProvider INSTANCE = new GroovyEngineProvider();
    }

    public static GroovyEngineProvider getInstance() {
        return ScriptEngineProviderHelper.INSTANCE;
    }

    @Override
    public void compile(String functionDefinition) throws ScriptException {
        CompiledScript compiled = compilable.compile(functionDefinition);
        compiled.eval(engine.getBindings(ENGINE_SCOPE));
    }

    @Override
    public void bind(Map<String, Object> bindingKeysAndValues) {

        Bindings bindings = engine.getBindings(ENGINE_SCOPE);

        bindingKeysAndValues.forEach(bindings::put);
    }

    @Override
    public void compile(Collection<String> moduleNames, Reader reader, Map<String, Object> customBindings) throws ScriptException {
        // We create a temporary binding object just to pass custom initialization bindings
        // to the module. They are not needed in the engine scope, but just for module initialization.
        Bindings tmpBindings = engine.createBindings();

        tmpBindings.putAll(customBindings);

        CompiledScript compiled = compilable.compile(reader);

        compiled.eval(tmpBindings);

        Bindings bindings = engine.getBindings(ENGINE_SCOPE);

        moduleNames.forEach(moduleName -> bindings.put(moduleName, tmpBindings.get(moduleName)));
    }

    @Override
    public Object invokeFunction(String functionName, Object... args) throws NoSuchMethodException, ScriptException {
        return invocable.invokeFunction(functionName, args);
    }

    @Override
    public void removeBinding(String bindingKey) {
        engine.getBindings(ENGINE_SCOPE).remove(bindingKey);
    }
}
