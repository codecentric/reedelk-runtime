package de.codecentric.reedelk.platform.component.trycatch;

import de.codecentric.reedelk.platform.execution.FlowExecutor;
import de.codecentric.reedelk.platform.execution.FlowExecutorFactory;
import de.codecentric.reedelk.platform.execution.MessageAndContext;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.component.TryCatch;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static de.codecentric.reedelk.platform.commons.NextNode.of;

public class TryCatchExecutor implements FlowExecutor {

    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {

        TryCatchWrapper tryCatch = (TryCatchWrapper) currentNode.getComponent();

        ExecutionNode firstTryNode = tryCatch.getFirstTryNode();

        ExecutionNode firstCatchNode = tryCatch.getFirstCatchNode();

        Flux<MessageAndContext> result = Flux.from(publisher).flatMap((Function<MessageAndContext, Mono<MessageAndContext>>) messageAndContext -> {

            Publisher<MessageAndContext> tryExecution =
                    FlowExecutorFactory.get().execute(Mono.just(messageAndContext), firstTryNode, graph);

            return Mono.from(tryExecution).onErrorResume(throwable -> {

                Mono<MessageAndContext> mapped = Mono.just(messageAndContext).map(context -> {

                    Message messageWithException = MessageBuilder.get(TryCatch.class)
                            .withJavaObject(throwable)
                            .build();

                    context.replaceWith(messageWithException);

                    return context;

                });

                return Mono.from(FlowExecutorFactory.get().execute(mapped, firstCatchNode, graph));

            });

        });

        ExecutionNode stopNode = tryCatch.getStopNode();

        // If the Router is followed by other nodes, then we keep executing
        // the other nodes, otherwise we stop and we return the current publisher.
        return of(stopNode, graph)
                .map(nodeAfterStop -> FlowExecutorFactory.get().execute(result, nodeAfterStop, graph))
                .orElse(result); // The Router is the last execution node of the flow.
    }
}
