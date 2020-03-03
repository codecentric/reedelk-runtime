package com.reedelk.esb.component.fork;

import com.reedelk.esb.commons.NextNode;
import com.reedelk.esb.execution.FlowExecutor;
import com.reedelk.esb.execution.FlowExecutorFactory;
import com.reedelk.esb.execution.MessageAndContext;
import com.reedelk.esb.execution.scheduler.SchedulerProvider;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.DefaultMessageAttributes;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.TypedContent;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Scheduler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ForkExecutor implements FlowExecutor {

    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {

        ForkWrapper fork = (ForkWrapper) currentNode.getComponent();

        List<ExecutionNode> nextExecutionNodes = fork.getForkNodes();

        ExecutionNode stopNode = fork.getStopNode();

        // If the stop node does not have next node, then it means that the Fork
        // is not followed by any other component.
        ExecutionNode nextAfterStop = NextNode.of(stopNode, graph).orElse(null);

        final Join join = getJoinComponentOrDefault(nextAfterStop);

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
            return Mono.zip(forkBranches, messagesCombinator())
                    .flatMap(eventsToJoin -> Mono.create(new JoinConsumer(messageContext, eventsToJoin, join)))
                    .publishOn(flowScheduler()); // switch back using another flow thread.
        });

        if (nextAfterStop == null) {
            // This is the last component of the flow, so we return the current publisher.
            return joinedForkFlux;
        }

        if (isJoinExecutionNode(nextAfterStop)) {
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

    private Function<Object[], MessageAndContext[]> messagesCombinator() {
        return objects -> {
            MessageAndContext[] messageAndContexts = new MessageAndContext[objects.length];
            for (int i = 0; i < objects.length; i++) {
                messageAndContexts[i] = (MessageAndContext) objects[i];
            }
            return messageAndContexts;
        };
    }

    private boolean isJoinExecutionNode(ExecutionNode nextAfterStop) {
        if (nextAfterStop != null) {
            Component joinComponent = nextAfterStop.getComponent();
            return joinComponent instanceof Join;
        }
        return false;
    }

    private Join getJoinComponentOrDefault(ExecutionNode nextAfterStop) {
        return Optional.ofNullable(nextAfterStop)
                .flatMap(executionNode -> executionNode.getComponent() instanceof Join ?
                        Optional.of((Join) executionNode.getComponent()) :
                        Optional.empty())
                .orElse(new EmptyJoin());
    }

    static class EmptyJoin implements Join {
        @Override
        public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {
            Map<String, Serializable> attributes = new HashMap<>();
            MessageAttributes emptyJoinAttributes = new DefaultMessageAttributes(EmptyJoin.class, attributes);
            return MessageBuilder.get().empty().attributes(emptyJoinAttributes).build();
        }
    }

    static class JoinConsumer implements Consumer<MonoSink<MessageAndContext>> {

        private final Join join;
        private final MessageAndContext context;
        private final MessageAndContext[] messages;

        JoinConsumer(MessageAndContext originalMessage, MessageAndContext[] messagesToJoin, Join join) {
            this.join = join;
            this.context = originalMessage;
            this.messages = messagesToJoin;
        }

        @Override
        public void accept(MonoSink<MessageAndContext> sink) {
            try {
                List<Message> collect = stream(messages)
                        .map(MessageAndContext::getMessage)
                        .collect(toList());

                Message outMessage = join.apply(context.getFlowContext(), collect);

                context.replaceWith(outMessage);

                sink.success(context);

            } catch (Exception exception) {
                // Propagate the error occurred while applying the join
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
        }
    }
}
