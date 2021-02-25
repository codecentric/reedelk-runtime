package de.codecentric.reedelk.platform.services.scriptengine.evaluator;

import de.codecentric.reedelk.platform.services.scriptengine.evaluator.function.FunctionDefinitionBuilder;
import de.codecentric.reedelk.platform.services.scriptengine.evaluator.function.FunctionDefinitionBuilderDefault;
import de.codecentric.reedelk.platform.services.scriptengine.evaluator.function.FunctionDefinitionBuilderLazy;
import de.codecentric.reedelk.runtime.api.commons.ScriptUtils;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.List;

import static de.codecentric.reedelk.platform.services.scriptengine.evaluator.ValueProviders.STREAM_PROVIDER;

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
                Publisher<T> publisher = execute(dynamicValue, STREAM_PROVIDER, FunctionDefinitionBuilderDefault.CONTEXT_AND_MESSAGE, flowContext, message);
                return TypedPublisher.from(publisher, dynamicValue.getEvaluatedType());
            }
        } else {
            // Not a script
            return TypedPublisher.from(Mono.justOrEmpty(dynamicValue.value()), dynamicValue.getEvaluatedType());
        }
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(DynamicValue<T> dynamicValue, FlowContext flowContext, Throwable throwable) {
        return evaluateStream(dynamicValue, FunctionDefinitionBuilderDefault.CONTEXT_AND_ERROR, flowContext, throwable);
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
