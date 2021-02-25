package de.codecentric.reedelk.platform.execution;

import de.codecentric.reedelk.platform.commons.NextNode;
import de.codecentric.reedelk.platform.execution.context.DefaultFlowContext;
import de.codecentric.reedelk.platform.execution.scheduler.SchedulerProvider;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.api.component.OnResult;
import de.codecentric.reedelk.runtime.api.message.Message;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

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

            ExecutionNode nodeAfterRoot = NextNode.ofOrThrow(root, graph);

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
