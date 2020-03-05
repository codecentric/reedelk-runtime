package com.reedelk.esb.component.fork;

import com.reedelk.esb.commons.NextNode;
import com.reedelk.esb.component.commons.Combinator;
import com.reedelk.esb.component.commons.JoinConsumer;
import com.reedelk.esb.component.commons.JoinUtils;
import com.reedelk.esb.execution.FlowExecutor;
import com.reedelk.esb.execution.FlowExecutorFactory;
import com.reedelk.esb.execution.MessageAndContext;
import com.reedelk.esb.execution.scheduler.SchedulerProvider;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.message.content.TypedContent;
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
            if (!content.isConsumed()) {
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
