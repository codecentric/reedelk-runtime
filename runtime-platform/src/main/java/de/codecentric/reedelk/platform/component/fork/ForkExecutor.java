package de.codecentric.reedelk.platform.component.fork;

import de.codecentric.reedelk.platform.commons.NextNode;
import de.codecentric.reedelk.platform.component.commons.Combinator;
import de.codecentric.reedelk.platform.component.commons.JoinConsumer;
import de.codecentric.reedelk.platform.component.commons.JoinUtils;
import de.codecentric.reedelk.platform.execution.FlowExecutor;
import de.codecentric.reedelk.platform.execution.FlowExecutorFactory;
import de.codecentric.reedelk.platform.execution.MessageAndContext;
import de.codecentric.reedelk.platform.execution.scheduler.SchedulerProvider;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.api.component.Join;
import de.codecentric.reedelk.runtime.api.message.content.TypedContent;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ForkExecutor implements FlowExecutor {

    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {

        ForkWrapper fork = (ForkWrapper) currentNode.getComponent();

        List<ExecutionNode> nextExecutionNodes = fork.getForkNodes();

        ExecutionNode stopNode = fork.getStopNode();

        // If the stop node does not have next node, then it means that
        // the Fork is not followed by any other component.
        ExecutionNode nextAfterStop = NextNode.of(stopNode, graph).orElse(null);

        final Join join = JoinUtils.joinComponentOrDefault(nextAfterStop);

        Flux<MessageAndContext> joinedForkFlux = Flux.from(publisher).flatMap(messageContext -> {

            // We must consume the message stream if it has not been consumed yet,
            // otherwise we cannot copy (by using serialization) its content and hand
            // it over to the Fork branches in the Message payload.
            TypedContent<?,?> content = messageContext.getMessage().content();
            if (content.isStream()) {
                content.consume();
            }

            // Create fork branches (Fork step)
            List<Mono<MessageAndContext>> forkBranches = nextExecutionNodes.stream()
                    .map(nextNode -> createForkBranch(nextNode, messageContext, graph, flowScheduler()))
                    .collect(toList());

            // Join fork branches (Join step)
            return Mono.zip(forkBranches, Combinator.messageAndContext())
                    .flatMap(eventsToJoin -> {
                        JoinConsumer joinConsumer = new JoinConsumer(messageContext, eventsToJoin, join);
                        return Mono.create(joinConsumer);
                    })
                    .publishOn(flowScheduler()); // switch back using another flow thread.
        });

        if (nextAfterStop == null) {
            // This is the last component of the flow, so we return the current publisher.
            return joinedForkFlux;
        }

        if (JoinUtils.isJoin(nextAfterStop)) {
            // 'nextAfterStop' is an execution node referring to a join node which was executed
            // as a result of the JoinConsumer above. Therefore, the next node to be executed must be
            // the next after the Join Execution Node.
            return NextNode.of(nextAfterStop, graph).map(nextOfJoin ->
                    FlowExecutorFactory.get().execute(joinedForkFlux, nextOfJoin, graph))
                    .orElse(joinedForkFlux);
        } else {
            // If 'nextAfterStop' was NOT a join,
            // then we keep with the execution starting from 'nextAfterStop'.
            return FlowExecutorFactory.get().execute(joinedForkFlux, nextAfterStop, graph);
        }
    }

    Scheduler flowScheduler() {
        return SchedulerProvider.flow();
    }

    private Mono<MessageAndContext> createForkBranch(ExecutionNode executionNode, MessageAndContext context, ExecutionGraph graph, Scheduler forkScheduler) {
        MessageAndContext messageCopy = context.copy();
        Mono<MessageAndContext> parent = Mono.just(messageCopy).publishOn(forkScheduler);
        Publisher<MessageAndContext> forkBranchPublisher = FlowExecutorFactory.get().execute(parent, executionNode, graph);
        return Mono.from(forkBranchPublisher);
    }
}
