package com.reedelk.esb.execution;

import com.reedelk.esb.configuration.RuntimeConfigurationProvider;
import com.reedelk.esb.execution.scheduler.SchedulerProvider;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.component.ProcessorAsync;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.Optional;

import static com.reedelk.esb.commons.NextNode.ofOrThrow;
import static java.time.Duration.ofMillis;

/**
 * Executes an asynchronous processor in a different Scheduler thread.
 * Waits for the processor to complete until any of the OnResult callback
 * is called by the implementing processor.
 */
public class ProcessorAsyncExecutor implements FlowExecutor {

    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {

        ProcessorAsync processorAsync = (ProcessorAsync) currentNode.getComponent();

        Publisher<MessageAndContext> parent = Flux.from(publisher).flatMap(event -> {

            // Build a Mono out of the async processor callback.
            Mono<MessageAndContext> callbackMono = sinkFromCallback(processorAsync, event)
                    .publishOn(flowScheduler());

            // If a timeout has been defined for the async processor callback,
            // then we set it here.
            return asyncCallbackTimeout()
                    .map(timeout -> callbackMono.timeout(ofMillis(timeout)))
                    .orElse(callbackMono);
        });

        ExecutionNode next = ofOrThrow(currentNode, graph);

        return FlowExecutorFactory.get().execute(parent, next, graph);
    }

    Scheduler flowScheduler() {
        return SchedulerProvider.flow();
    }

    /**
     * Returns optionally the async processor timeout value.
     * Note that if the timeout is < 0, then the timeout is disabled.
     */
    Optional<Long> asyncCallbackTimeout() {
        long asyncProcessorTimeout = RuntimeConfigurationProvider.get()
                .getFlowExecutorConfig()
                .asyncProcessorTimeout();
        return asyncProcessorTimeout < 0 ?
                Optional.empty() :
                Optional.of(asyncProcessorTimeout);
    }

    private static Mono<MessageAndContext> sinkFromCallback(ProcessorAsync processor, MessageAndContext event) {
        return Mono.create(sink -> {

            // Prepare the callback
            OnResult callback = new OnResult() {
                @Override
                public void onResult(FlowContext context, Message message) {
                    event.replaceWith(message);
                    sink.success(event);
                }
                @Override
                public void onError(FlowContext context, Throwable e) {
                    sink.error(e);
                }
            };

            // Apply the processor
            try {
                processor.apply(event.getFlowContext(), event.getMessage(), callback);

            } catch (Exception exception) {
                // Propagate the error occurred while applying the processor
                sink.error(exception);

            } catch (Throwable throwable) {
                // If Throwable is 'NoClassDefFoundError' it means that the processor uses a component
                // which uses a class not imported in the module bundle it belongs to.
                // The module bundle should correctly add the missing import in the pom.xml configuration:
                // maven-bundle-plugin > configuration > instructions > Import-Package xml node.
                // IMPORTANT: This exception must be wrapped because Throwable exceptions are NOT
                // caught in the com.reedelk.esb.execution.FlowExecutorEngine -> doOnError callback.
                // They are normally caught at subscribe time however it does not apply because
                // the FlowExecutorEngine the stream on publishOn(SchedulerProvider.flow())
                // - i.e on a different thread. Therefore we must wrap 'NoClassDefFoundError' exception.
                ESBException wrapped = new ESBException(throwable);
                sink.error(wrapped);
            }
        });
    }
}
