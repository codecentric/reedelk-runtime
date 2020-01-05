package com.reedelk.esb.services.scriptengine.evaluator;

import com.reedelk.esb.services.scriptengine.evaluator.function.FunctionDefinitionBuilder;
import com.reedelk.esb.services.scriptengine.evaluator.function.ScriptDefinitionBuilder;
import com.reedelk.runtime.api.message.content.utils.TypedPublisher;
import com.reedelk.runtime.api.script.Script;
import org.reactivestreams.Publisher;

import java.util.Optional;

import static com.reedelk.esb.services.scriptengine.evaluator.ValueProviders.OPTIONAL_PROVIDER;
import static com.reedelk.esb.services.scriptengine.evaluator.ValueProviders.STREAM_PROVIDER;

@SuppressWarnings("unchecked")
public class ScriptEvaluator extends AbstractDynamicValueEvaluator {

    private final FunctionDefinitionBuilder scriptDefinitionBuilder;

    public ScriptEvaluator() {
        scriptDefinitionBuilder = new ScriptDefinitionBuilder();
    }

    @Override
    public <T> Optional<T> evaluate(Script script, Class<T> returnType, Object ...args) {
        if (script == null || script.isEmpty()) {
            return OPTIONAL_PROVIDER.empty();
        } else {
            return (Optional<T>) evaluateScript(script, returnType, OPTIONAL_PROVIDER, args);
        }
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(Script script, Class<T> returnType, Object ...args) {
        if (script == null) {
            return null;
        } else if (script.isEmpty()) {
            return TypedPublisher.from(STREAM_PROVIDER.empty(), returnType);
        } else {
            Publisher<T> resultPublisher = (Publisher<T>) evaluateScript(script, returnType, STREAM_PROVIDER, args);
            return TypedPublisher.from(resultPublisher, returnType);
        }
    }

    private <T> T evaluateScript(Script script, Class<T> returnType, ValueProvider valueProvider, Object ...args) {
        Object evaluationResult = invokeFunction(script, scriptDefinitionBuilder, args);
        return convert(evaluationResult, returnType, valueProvider);
    }
}
