package com.reedelk.esb.services.scriptengine.evaluator;

import com.reedelk.esb.services.converter.DefaultConverterService;
import com.reedelk.esb.services.scriptengine.evaluator.function.DynamicMapWithErrorAndContext;
import com.reedelk.esb.services.scriptengine.evaluator.function.DynamicMapWithMessageAndContext;
import com.reedelk.esb.services.scriptengine.evaluator.function.FunctionDefinitionBuilder;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.script.dynamicmap.DynamicMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class DynamicMapEvaluator extends AbstractDynamicValueEvaluator {

    private final FunctionDefinitionBuilder errorFunctionBuilder = new DynamicMapWithErrorAndContext();
    private final FunctionDefinitionBuilder functionBuilder = new DynamicMapWithMessageAndContext();
    private final Map<String, ?> emptyMap = Collections.unmodifiableMap(Collections.emptyMap());

    @Override
    public <T> Map<String, T> evaluate(DynamicMap<T> dynamicMap, FlowContext context, Message message) {
        return evaluateWith(dynamicMap, functionBuilder, message, context);
    }

    @Override
    public <T> Map<String, T> evaluate(DynamicMap<T> dynamicMap, FlowContext context, Throwable throwable) {
        return evaluateWith(dynamicMap, errorFunctionBuilder, throwable, context);
    }

    private <T> Map<String, T> evaluateWith(DynamicMap<T> dynamicMap, FunctionDefinitionBuilder<DynamicMap<T>> functionBuilder, Object... args) {
        if (dynamicMap == null || dynamicMap.isEmpty()) {
            // If dynamic map is empty, nothing to do.
            return (Map<String, T>) emptyMap;

        } else {
            Map<String, T> evaluatedMap = (Map<String, T>) invokeFunction(dynamicMap, functionBuilder, args);
            Map<String, T> result = new HashMap<>();
            evaluatedMap.forEach((key, value) -> {
                // We map the values to the correct output value type
                T converted = DefaultConverterService.getInstance().convert(value, dynamicMap.getEvaluatedType());
                result.put(key, converted);
            });
            return result;
        }
    }
}
