package com.reedelk.esb.services.scriptengine.evaluator;

import com.reedelk.esb.exception.ScriptCompilationException;
import com.reedelk.esb.exception.ScriptExecutionException;
import com.reedelk.esb.pubsub.Event;
import com.reedelk.esb.pubsub.OnMessage;
import com.reedelk.esb.services.converter.DefaultConverterService;
import com.reedelk.esb.services.scriptengine.JavascriptEngineProvider;
import com.reedelk.esb.services.scriptengine.evaluator.function.FunctionDefinitionBuilder;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.script.ScriptBlock;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import org.reactivestreams.Publisher;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.reedelk.esb.pubsub.Action.Module.ActionModuleUninstalled;
import static com.reedelk.esb.pubsub.Action.Module.UN_INSTALLED;
import static com.reedelk.esb.services.scriptengine.evaluator.ValueProviders.STREAM_PROVIDER;

abstract class AbstractDynamicValueEvaluator extends ScriptEngineServiceAdapter {

    final Map<Long, List<String>> moduleIdFunctionNamesMap = new HashMap<>();

    AbstractDynamicValueEvaluator() {
        Event.operation.subscribe(UN_INSTALLED, this);
    }

    <S, T> S execute(DynamicValue<T> dynamicValue, ValueProvider provider, FunctionDefinitionBuilder<DynamicValue<?>> functionDefinitionBuilder, Object... args) {
        if (dynamicValue.isEmpty()) {
            return provider.empty();
        } else {
            Object evaluationResult = invokeFunction(dynamicValue, functionDefinitionBuilder, args);
            return convert(evaluationResult, dynamicValue.getEvaluatedType(), provider);
        }
    }

    <S> S convert(Object value, Class<?> targetClazz, ValueProvider provider) {
        if (value == null) {
            return provider.empty();
        } else if (value instanceof TypedPublisher<?>) {
            // Value is a typed stream
            TypedPublisher<?> typedPublisher = (TypedPublisher<?>) value;
            Object converted = converterService().convert(typedPublisher, targetClazz);
            return provider.from(converted);
        } else {
            // Value is NOT a typed stream
            Object converted = converterService().convert(value, targetClazz);
            return provider.from(converted);
        }
    }

    /**
     * Evaluate the payload without invoking the script engine. This is an optimization
     * since we can get the payload directly from Java without making an expensive call
     * to the script engine.
     */
    <T> TypedPublisher<T> evaluateMessagePayload(Class<T> targetType, Message message) {
        if (message.content().isStream()) {
            // We don't resolve the stream, but we still might need to
            // map its content from source type to a target type.
            TypedPublisher<?> stream = message.content().stream();
            return convert(stream, targetType, STREAM_PROVIDER);
        } else {
            Publisher<T> converted = convert(message.payload(), targetType, STREAM_PROVIDER);
            return TypedPublisher.from(converted, targetType);
        }
    }

    <T extends ScriptBlock> Object invokeFunction(T scriptBlock, FunctionDefinitionBuilder<T> functionDefinitionBuilder, Object... args) {
        try {

            return scriptEngine().invokeFunction(scriptBlock.functionName(), args);

        }  catch (ScriptException scriptException) {
            // We add to the original exception the body of the script so that
            // it will be easy to identify which script failed in the flow.
            throw new ScriptExecutionException(scriptBlock, scriptException);

        } catch (NoSuchMethodException e) {
            // The function has not been compiled yet, optimistic invocation
            // failed. We compile the function and try to invoke it again.
            compile(scriptBlock, functionDefinitionBuilder);

            try {

                return scriptEngine().invokeFunction(scriptBlock.functionName(), args);

            }  catch (ScriptException | NoSuchMethodException scriptException) {
                // We add some contextual information to the original exception such as
                // module if, flow id, flow title and script body which failed the execution.

                // If no such method exception was again thrown, it means
                // that something went wrong in the engine. In this case
                // there is nothing we can do to fix it and therefore
                // we rethrow the exception to the caller.
                throw new ScriptExecutionException(scriptBlock, scriptException);
            }
        }
    }

    <T extends ScriptBlock> void compile(T scriptBlock, FunctionDefinitionBuilder<T> functionDefinitionBuilder) {
        synchronized (this) {

            long moduleId = scriptBlock.getContext().getModuleId();

            if (!moduleIdFunctionNamesMap.containsKey(moduleId)) {
                moduleIdFunctionNamesMap.put(moduleId, new ArrayList<>());
            }
            String functionName = scriptBlock.functionName();

            if (moduleIdFunctionNamesMap.get(moduleId).contains(functionName)) {
                // Already compiled by a previous call. This is needed because
                // compile might have been called by multiple Threads for the
                // same function, we prevent the function to be compiled twice.
                return;
            }

            String functionDefinition = functionDefinitionBuilder.apply(scriptBlock);
            try {
                scriptEngine().compile(functionDefinition);
            } catch (ScriptException scriptCompilationException) {
                throw new ScriptCompilationException(scriptBlock, scriptCompilationException);
            }

            // Compilation was successful, we can add the function name
            // to the list of functions registered for the given module id.
            moduleIdFunctionNamesMap.get(moduleId).add(functionName);
        }
    }

    @OnMessage
    public void onModuleUninstalled(ActionModuleUninstalled action) {
        // No need to synchronize the access to 'moduleIdFunctionNamesMap' because
        // this method is called always AFTER a module has been completely stopped,
        // hence we are sure that none of its functions might be called.
        long moduleId = action.getMessage();
        if (moduleIdFunctionNamesMap.containsKey(moduleId)) {
            moduleIdFunctionNamesMap.remove(moduleId)
                    .forEach(computedFunctionName ->
                            scriptEngine().unDefineFunction(computedFunctionName));
        }
    }

    ScriptEngineProvider scriptEngine() {
        return JavascriptEngineProvider.getInstance();
    }

    private ConverterService converterService() {
        return DefaultConverterService.getInstance();
    }
}
