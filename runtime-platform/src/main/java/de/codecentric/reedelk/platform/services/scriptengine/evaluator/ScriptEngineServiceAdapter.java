package de.codecentric.reedelk.platform.services.scriptengine.evaluator;

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

public class ScriptEngineServiceAdapter implements ScriptEngineService {

    // Dynamic value

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> value, FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, MimeType mimeType, FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> value, FlowContext flowContext, Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, List<String> argumentNames, Object... bindings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, MimeType mimeType, List<String> argumentNames, Object... bindings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> value, FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, List<String> argumentNames, Object... bindings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> value, FlowContext flowContext, Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    // Script

    @Override
    public <T> Optional<T> evaluate(Script script, Class<T> returnType, Object ...args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(Script script, Class<T> returnType, Object ...args) {
        throw new UnsupportedOperationException();
    }

    // Dynamic map

    @Override
    public <T> Map<String, T> evaluate(DynamicMap<T> dynamicMap, FlowContext context, Message message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Map<String, T> evaluate(DynamicMap<T> dynamicMap, FlowContext context, Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    // Register Function/s and Script Sources.

    @Override
    public void register(ScriptGlobalFunctions globalFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void register(ScriptSource scriptSource) {
        throw new UnsupportedOperationException();
    }
}
