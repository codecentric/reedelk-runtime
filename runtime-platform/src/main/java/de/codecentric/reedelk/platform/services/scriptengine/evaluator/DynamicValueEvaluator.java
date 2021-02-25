package de.codecentric.reedelk.platform.services.scriptengine.evaluator;

import de.codecentric.reedelk.platform.services.scriptengine.evaluator.function.FunctionDefinitionBuilder;
import de.codecentric.reedelk.platform.services.scriptengine.evaluator.function.FunctionDefinitionBuilderDefault;
import de.codecentric.reedelk.platform.services.scriptengine.evaluator.function.FunctionDefinitionBuilderLazy;
import de.codecentric.reedelk.runtime.api.commons.ScriptUtils;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.List;
import java.util.Optional;

public class DynamicValueEvaluator extends AbstractDynamicValueEvaluator {

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, FlowContext flowContext, Message message) {
        if (dynamicValue == null) {
            // Value is not present
            return ValueProviders.OPTIONAL_PROVIDER.empty();
        } else if (dynamicValue.isScript()) {
            // Script
            if (ScriptUtils.isEvaluateMessagePayload(dynamicValue)) {
                // we avoid evaluating the payload with the script engine (optimization)
                // note that by calling message.payload(), if it is a stream we are
                // automatically resolving it.
                Object payload = message.payload();
                return convert(payload, dynamicValue.getEvaluatedType(), ValueProviders.OPTIONAL_PROVIDER);
            } else {
                return execute(dynamicValue, ValueProviders.OPTIONAL_PROVIDER, FunctionDefinitionBuilderDefault.CONTEXT_AND_MESSAGE, flowContext, message);
            }
        } else {
            // Not a script
            return Optional.ofNullable(dynamicValue.value());
        }
    }

    /**
     * The mime type argument is useful when we have a DynamicObject value and a Mime Type in the component.
     * We allow the result of the evaluation to be any object however, we want to convert it to the best suitable
     * type starting from the mime type.
     */
    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, MimeType mimeType, FlowContext flowContext, Message message) {
        if (dynamicValue == null) {
            // Value is not present
            return ValueProviders.OPTIONAL_PROVIDER.empty();
        } else if (dynamicValue.isScript()) {
            if (dynamicValue.isEmpty()) {
                return ValueProviders.OPTIONAL_PROVIDER.empty();
                // Script
            } else if (ScriptUtils.isEvaluateMessagePayload(dynamicValue)) {
                // we avoid evaluating the payload with the script engine (optimization)
                // note that by calling message.payload(), if it is a stream we are
                // automatically resolving it.
                Object payload = message.payload();
                return convert(payload, mimeType.javaType(), ValueProviders.OPTIONAL_PROVIDER);
            } else {
                Object evaluationResult = invokeFunction(dynamicValue, FunctionDefinitionBuilderDefault.CONTEXT_AND_MESSAGE, flowContext, message);
                return convert(evaluationResult, mimeType.javaType(), ValueProviders.OPTIONAL_PROVIDER);
            }
        } else {
            // Not a script
            return convert(dynamicValue.value(), mimeType.javaType(), ValueProviders.OPTIONAL_PROVIDER);
        }
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, FlowContext flowContext, Throwable exception) {
        return evaluate(dynamicValue, FunctionDefinitionBuilderDefault.CONTEXT_AND_ERROR, flowContext, exception);
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, List<String> argumentNames, Object... bindings) {
        return evaluate(dynamicValue, FunctionDefinitionBuilderLazy.from(argumentNames), bindings);
    }

    @Override
    public <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, MimeType mimeType, List<String> argumentNames, Object... bindings) {
        if (dynamicValue == null) {
            // Value is not present
            return ValueProviders.OPTIONAL_PROVIDER.empty();
        } else if (dynamicValue.isScript()) {
            if (dynamicValue.isEmpty()) {
                return ValueProviders.OPTIONAL_PROVIDER.empty();
            } else {
                // Script
                Object evaluationResult = invokeFunction(dynamicValue, FunctionDefinitionBuilderLazy.from(argumentNames), bindings);
                return convert(evaluationResult, mimeType.javaType(), ValueProviders.OPTIONAL_PROVIDER);
            }
        } else {
            // Not a script
            return convert(dynamicValue.value(), mimeType.javaType(), ValueProviders.OPTIONAL_PROVIDER);
        }
    }

    private <T> Optional<T> evaluate(DynamicValue<T> dynamicValue, FunctionDefinitionBuilder<DynamicValue<?>> functionDefinitionBuilder, Object... bindings) {
        if (dynamicValue == null) {
            // Value is not present
            return ValueProviders.OPTIONAL_PROVIDER.empty();
        } else if (dynamicValue.isScript()) {
            return execute(dynamicValue, ValueProviders.OPTIONAL_PROVIDER, functionDefinitionBuilder, bindings);
        } else {
            // Not a script
            return Optional.ofNullable(dynamicValue.value());
        }
    }
}
