package com.reedelk.esb.execution;

import com.reedelk.esb.execution.scheduler.SchedulerProvider;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.message.Message;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import static com.reedelk.esb.execution.ExecutionUtils.nextNodeOfOrThrow;

public class FlowExecutorEngine {

    private final ExecutionGraph graph;

    public FlowExecutorEngine(ExecutionGraph graph) {
        this.graph = graph;
    }

    /**
     * Executes the flow when an Event comes in.
     */
    public void onEvent(Message message, OnResult onResult) {

        DefaultFlowContext defaultContext = DefaultFlowContext.from(message);

        try {

            MessageAndContext event = new MessageAndContext(message, defaultContext);

            Publisher<MessageAndContext> publisher = Mono.just(event).publishOn(scheduler());

            ExecutionNode root = graph.getRoot();

            ExecutionNode nodeAfterRoot = nextNodeOfOrThrow(root, graph);

            Publisher<MessageAndContext> result =
                    FlowExecutorFactory.get().execute(publisher, nodeAfterRoot, graph);

            Mono.from(result)
                    .doOnError(throwable -> onResult.onError(defaultContext, throwable))
                    .subscribe(messageContext -> onResult.onResult(defaultContext, messageContext.getMessage()));

        } catch (Throwable exception) {
            onResult.onError(defaultContext, exception);
        }
    }

    Scheduler scheduler() {
        return SchedulerProvider.flow();
    }
}