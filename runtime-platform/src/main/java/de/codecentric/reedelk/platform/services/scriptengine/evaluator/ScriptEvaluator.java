package de.codecentric.reedelk.platform.services.scriptengine.evaluator;

import de.codecentric.reedelk.platform.services.scriptengine.evaluator.function.FunctionDefinitionBuilder;
import de.codecentric.reedelk.platform.services.scriptengine.evaluator.function.ScriptDefinitionBuilder;
import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.api.script.Script;
import org.reactivestreams.Publisher;

import java.util.Optional;

@SuppressWarnings("unchecked")
public class ScriptEvaluator extends AbstractDynamicValueEvaluator {

    private final FunctionDefinitionBuilder<Script> scriptDefinitionBuilder = new ScriptDefinitionBuilder();


    @Override
    public <T> Optional<T> evaluate(Script script, Class<T> returnType, Object ...args) {
        if (script == null || script.isEmpty()) {
            return ValueProviders.OPTIONAL_PROVIDER.empty();
        } else {
            return (Optional<T>) evaluateScript(script, returnType, ValueProviders.OPTIONAL_PROVIDER, args);
        }
    }

    @Override
    public <T> TypedPublisher<T> evaluateStream(Script script, Class<T> returnType, Object ...args) {
        if (script == null) {
            return null;
        } else if (script.isEmpty()) {
            return TypedPublisher.from(ValueProviders.STREAM_PROVIDER.empty(), returnType);
        } else {
            Publisher<T> resultPublisher = (Publisher<T>) evaluateScript(script, returnType, ValueProviders.STREAM_PROVIDER, args);
            return TypedPublisher.from(resultPublisher, returnType);
        }
    }

    private <T> T evaluateScript(Script script, Class<T> returnType, ValueProvider valueProvider, Object ...args) {
        Object evaluationResult = invokeFunction(script, scriptDefinitionBuilder, args);
        return convert(evaluationResult, returnType, valueProvider);
    }
}
