package com.reedelk.esb.services.scriptengine.evaluator;

import com.reedelk.esb.services.scriptengine.evaluator.function.DynamicValueWithErrorAndContext;
import com.reedelk.esb.services.scriptengine.evaluator.function.DynamicValueWithMessageAndContext;
import com.reedelk.runtime.api.commons.ScriptUtils;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.Optional;

import static com.reedelk.esb.services.scriptengine.evaluator.ValueProviders.OPTIONAL_PROVIDER;

public class DynamicValueEvaluator extends AbstractDynamicValueEvaluator {

    private final DynamicValueWithErrorAndContext errorFunctionBuilder = new DynamicValueWithErrorAndContext();
    private final DynamicValueWithMessageAndContext functionBuilder = new DynamicValueWithMessageAndContext();

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, FlowContext flowContext, Message message) {
        if (dynamicValue == null) {
            // Value is not present
            return OPTIONAL_PROVIDER.empty();
        } else if (dynamicValue.isScript()) {
            // Script
            if (ScriptUtils.isEvaluateMessagePayload(dynamicValue)) {
                // we avoid evaluating the payload with the script engine (optimization)
                // note that by calling message.payload(), if it is a stream we are
                // automatically resolving it.
                Object payload = message.payload();
                return convert(payload, dynamicValue.getEvaluatedType(), OPTIONAL_PROVIDER);
            } else {
                return execute(dynamicValue, OPTIONAL_PROVIDER, functionBuilder, flowContext, message);
            }
        } else {
            // Not a script
            return Optional.ofNullable(dynamicValue.value());
        }
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, FlowContext flowContext, Throwable exception) {
        if (dynamicValue == null) {
            // Value is not present
            return OPTIONAL_PROVIDER.empty();
        } else if (dynamicValue.isScript()) {
            // Script
            return execute(dynamicValue, OPTIONAL_PROVIDER, errorFunctionBuilder, flowContext, exception);
        } else {
            // Not a script
            return Optional.ofNullable(dynamicValue.value());
        }
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, MimeType mimeType, FlowContext flowContext, Message message) {
        if (dynamicValue == null) {
            // Value is not present
            return OPTIONAL_PROVIDER.empty();
        } else if (dynamicValue.isScript()) {
            if (dynamicValue.isEmpty()) {
                return OPTIONAL_PROVIDER.empty();
                // Script
            } else if (ScriptUtils.isEvaluateMessagePayload(dynamicValue)) {
                // we avoid evaluating the payload with the script engine (optimization)
                // note that by calling message.payload(), if it is a stream we are
                // automatically resolving it.
                Object payload = message.payload();
                return convert(payload, mimeType.javaType(), OPTIONAL_PROVIDER);
            } else {
                Object evaluationResult = invokeFunction(dynamicValue, functionBuilder, flowContext, message);
                return convert(evaluationResult, mimeType.javaType(), OPTIONAL_PROVIDER);
            }
        } else {
            return convert(dynamicValue.value(), mimeType.javaType(), OPTIONAL_PROVIDER);
        }
    }
}
