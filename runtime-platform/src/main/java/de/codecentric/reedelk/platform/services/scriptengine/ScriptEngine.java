package de.codecentric.reedelk.platform.services.scriptengine;

import de.codecentric.reedelk.platform.services.scriptengine.evaluator.*;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.api.script.Script;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import de.codecentric.reedelk.runtime.api.script.ScriptGlobalFunctions;
import de.codecentric.reedelk.runtime.api.script.ScriptSource;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ScriptEngine implements ScriptEngineService {

    private static final ScriptEngine INSTANCE = new ScriptEngine();

    private DynamicValueStreamEvaluator dynamicValueStreamEvaluator;
    private DynamicValueEvaluator dynamicValueEvaluator;
    private ScriptSourceEvaluator scriptSourceEvaluator;
    private DynamicMapEvaluator dynamicMapEvaluator;
    private ScriptEvaluator scriptEvaluator;

    private ScriptEngine() {
        dynamicValueStreamEvaluator = new DynamicValueStreamEvaluator();
        dynamicValueEvaluator = new DynamicValueEvaluator();
        scriptSourceEvaluator = new ScriptSourceEvaluator();
        dynamicMapEvaluator = new DynamicMapEvaluator();
        scriptEvaluator = new ScriptEvaluator();
    }

    public static ScriptEngine getInstance() {
        return INSTANCE;
    }

    // Dynamic value

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, FlowContext flowContext, Message message) {
        return dynamicValueEvaluator.evaluate(dynamicValue, flowContext, message);
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, FlowContext flowContext, Throwable exception) {
        return dynamicValueEvaluator.evaluate(dynamicValue, flowContext, exception);
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicObject, MimeType mimeType, FlowContext flowContext, Message message) {
        return dynamicValueEvaluator.evaluate(dynamicObject, mimeType, flowContext, message);
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, List<String> argumentNames, Object... bindings) {
        return dynamicValueEvaluator.evaluate(dynamicValue, argumentNames, bindings);
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, MimeType mimeType, List<String> argumentNames, Object... bindings) {
        return dynamicValueEvaluator.evaluate(dynamicValue, mimeType, argumentNames, bindings);
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, FlowContext flowContext, Throwable throwable) {
        return dynamicValueStreamEvaluator.evaluateStream(dynamicValue, flowContext, throwable);
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, FlowContext flowContext, Message message) {
        return dynamicValueStreamEvaluator.evaluateStream(dynamicValue, flowContext, message);
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, List<String> argumentNames, Object... bindings) {
        return dynamicValueStreamEvaluator.evaluateStream(dynamicValue, argumentNames, bindings);
    }

    // Script

    @Override
    public <T> Optional<T> evaluate(Script script, Class<T> returnType, Object ...args) {
        return scriptEvaluator.evaluate(script, returnType, args);
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(Script script, Class<T> returnType, Object ...args) {
        return scriptEvaluator.evaluateStream(script, returnType, args);
    }

    // Dynamic map

    @Override
    public <T> Map<String, T> evaluate(DynamicMap<T> dynamicMap, FlowContext context, Message message) {
        return dynamicMapEvaluator.evaluate(dynamicMap, context, message);
    }

    @Override
    public <T> Map<String, T> evaluate(DynamicMap<T> dynamicMap, FlowContext context, Throwable throwable) {
        return dynamicMapEvaluator.evaluate(dynamicMap, context, throwable);
    }

    // Register Function/s and Script Sources.

    @Override
    public void register(ScriptGlobalFunctions globalFunction) {
        scriptSourceEvaluator.register(globalFunction);
    }

    @Override
    public void register(ScriptSource scriptSource) {
        scriptSourceEvaluator.register(scriptSource);
    }
}
