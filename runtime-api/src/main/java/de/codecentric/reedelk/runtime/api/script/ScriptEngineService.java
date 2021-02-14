package de.codecentric.reedelk.runtime.api.script;

import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ScriptEngineService {

    // Dynamic value

    <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, FlowContext flowContext, Message message);

    <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, MimeType mimeType, FlowContext flowContext, Message message);

    <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, List<String> argumentNames, Object ...bindings);

    <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, MimeType mimeType, List<String> argumentNames, Object ...bindings);

    <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, FlowContext flowContext, Throwable throwable);

    <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, FlowContext flowContext, Message message);

    <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, List<String> argumentNames, Object ...bindings);

    <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, FlowContext flowContext, Throwable throwable);

    // Script

    <T> Optional<T> evaluate(Script script, Class<T> returnType, Object ...args);

    <T> TypedPublisher<T> evaluateStream(Script script, Class<T> returnType, Object ...args);

    // Dynamic map

    <T> Map<String, T> evaluate(DynamicMap<T> dynamicMap, FlowContext context, Message message);

    <T> Map<String, T> evaluate(DynamicMap<T> dynamicMap, FlowContext context, Throwable throwable);

    // Register Function/s and Script Sources.

    void register(ScriptGlobalFunctions globalFunction);

    void register(ScriptSource scriptSource);

}
