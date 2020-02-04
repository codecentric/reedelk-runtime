package com.reedelk.esb.services.scriptengine;

import com.reedelk.esb.services.scriptengine.evaluator.*;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.script.Script;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.ScriptSource;
import com.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

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
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, FlowContext flowContext, Throwable throwable) {
        return dynamicValueStreamEvaluator.evaluateStream(dynamicValue, flowContext, throwable);
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, FlowContext flowContext, Message message) {
        return dynamicValueStreamEvaluator.evaluateStream(dynamicValue, flowContext, message);
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

    // Register Function

    @Override
    public void register(ScriptSource scriptSource) {
        scriptSourceEvaluator.register(scriptSource);
    }
}