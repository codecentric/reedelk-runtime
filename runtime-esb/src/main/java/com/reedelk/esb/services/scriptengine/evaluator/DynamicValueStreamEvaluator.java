package com.reedelk.esb.services.scriptengine.evaluator;

import com.reedelk.esb.services.scriptengine.evaluator.function.FunctionDefinitionBuilder;
import com.reedelk.esb.services.scriptengine.evaluator.function.FunctionDefinitionBuilderLazy;
import com.reedelk.runtime.api.commons.ScriptUtils;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.reedelk.esb.services.scriptengine.evaluator.ValueProviders.STREAM_PROVIDER;
import static com.reedelk.esb.services.scriptengine.evaluator.function.FunctionDefinitionBuilderDefault.CONTEXT_AND_ERROR;
import static com.reedelk.esb.services.scriptengine.evaluator.function.FunctionDefinitionBuilderDefault.CONTEXT_AND_MESSAGE;

public class DynamicValueStreamEvaluator extends AbstractDynamicValueEvaluator {

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, FlowContext flowContext, Message message) {
        if (dynamicValue == null) {
            // Value is not present
            return null;
        } else if (dynamicValue.isScript()) {
            // Script
            if (ScriptUtils.isEvaluateMessagePayload(dynamicValue)) {
                return evaluateMessagePayload(dynamicValue.getEvaluatedType(), message);
            } else {
                Publisher<T> publisher = execute(dynamicValue, STREAM_PROVIDER, CONTEXT_AND_MESSAGE, flowContext, message);
                return TypedPublisher.from(publisher, dynamicValue.getEvaluatedType());
            }
        } else {
            // Not a script
            return TypedPublisher.from(Mono.justOrEmpty(dynamicValue.value()), dynamicValue.getEvaluatedType());
        }
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, FlowContext flowContext, Throwable throwable) {
        return evaluateStream(dynamicValue, CONTEXT_AND_ERROR, flowContext, throwable);
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, List<String> argumentNames, Object... bindings) {
        return evaluateStream(dynamicValue, FunctionDefinitionBuilderLazy.from(argumentNames), bindings);
    }

    private  <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, FunctionDefinitionBuilder<DynamicValue<?>> functionDefinitionBuilder, Object... bindings) {
        if (dynamicValue == null) {
            // Value is not present
            return null;
        } else if (dynamicValue.isScript()) {
            // Script
            return TypedPublisher.from(
                    execute(dynamicValue, STREAM_PROVIDER, functionDefinitionBuilder, bindings),
                    dynamicValue.getEvaluatedType());
        } else {
            // Not a script
            return TypedPublisher.from(Mono.justOrEmpty(dynamicValue.value()), dynamicValue.getEvaluatedType());
        }
    }
}
