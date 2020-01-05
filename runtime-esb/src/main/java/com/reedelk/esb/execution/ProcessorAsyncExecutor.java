package com.reedelk.esb.execution;

import com.reedelk.esb.configuration.RuntimeConfigurationProvider;
import com.reedelk.esb.execution.scheduler.SchedulerProvider;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.component.ProcessorAsync;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.Optional;

import static com.reedelk.esb.execution.ExecutionUtils.nextNodeOfOrThrow;
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

        ExecutionNode next = nextNodeOfOrThrow(currentNode, graph);

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
            OnResult callback = new OnResult() {
                @Override
                public void onResult(Message message, FlowContext context) {
                    event.replaceWith(message);
                    sink.success(event);
                }

                @Override
                public void onError(Throwable e, FlowContext context) {
                    sink.error(e);
                }
            };

            try {
                processor.apply(event.getMessage(), event.getFlowContext(), callback);
            } catch (Exception e) {
                sink.error(e);
            }
        });
    }
}