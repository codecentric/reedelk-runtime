package com.reedelk.esb.services.scriptengine.evaluator;

import com.reedelk.esb.services.scriptengine.evaluator.function.DynamicValueWithErrorAndContext;
import com.reedelk.esb.services.scriptengine.evaluator.function.DynamicValueWithMessageAndContext;
import com.reedelk.runtime.api.commons.ScriptUtils;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import static com.reedelk.esb.services.scriptengine.evaluator.ValueProviders.STREAM_PROVIDER;

public class DynamicValueStreamEvaluator extends AbstractDynamicValueEvaluator {

    private final DynamicValueWithErrorAndContext errorFunctionBuilder = new DynamicValueWithErrorAndContext();
    private final DynamicValueWithMessageAndContext functionBuilder = new DynamicValueWithMessageAndContext();

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
                Publisher<T> publisher = execute(dynamicValue, STREAM_PROVIDER, functionBuilder, flowContext, message);
                return TypedPublisher.from(publisher, dynamicValue.getEvaluatedType());
            }
        } else {
            // Not a script
            return TypedPublisher.from(Mono.justOrEmpty(dynamicValue.value()), dynamicValue.getEvaluatedType());
        }
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, FlowContext flowContext, Throwable throwable) {
        if (dynamicValue == null) {
            // Value is not present
            return null;
        } else if (dynamicValue.isScript()) {
            // Script
            return TypedPublisher.from(
                    execute(dynamicValue, STREAM_PROVIDER, errorFunctionBuilder, flowContext, throwable),
                    dynamicValue.getEvaluatedType());
        } else {
            // Not a script
            return TypedPublisher.from(Mono.justOrEmpty(dynamicValue.value()), dynamicValue.getEvaluatedType());
        }
    }
}