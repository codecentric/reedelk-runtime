package com.reedelk.runtime.api.script;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

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

    // Register Function

    void register(ScriptSource scriptSource);

}
