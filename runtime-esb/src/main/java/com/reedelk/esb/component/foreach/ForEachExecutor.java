package com.reedelk.esb.component.foreach;

import com.reedelk.esb.commons.NextNode;
import com.reedelk.esb.component.fork.ForkExecutor;
import com.reedelk.esb.execution.FlowExecutor;
import com.reedelk.esb.execution.FlowExecutorFactory;
import com.reedelk.esb.execution.MessageAndContext;
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
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

// TODO: This is all duplicated
public class ForEachExecutor implements FlowExecutor {

    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {

        ForEachWrapper forEach = (ForEachWrapper) currentNode.getComponent();

        ExecutionNode firstEachNode = forEach.getFirstEachNode();

        ExecutionNode stopNode = forEach.getStopNode();

        // If the stop node does not have next node, then it means that the for each
        // is not followed by any other component.
        ExecutionNode nextAfterStop = NextNode.of(stopNode, graph).orElse(null);

        final Join join = joinComponentOrDefault(nextAfterStop);

        Flux<MessageAndContext> aggregatedForEachResults = Flux.from(publisher).flatMap(messageAndContext -> {
            Message message = messageAndContext.getMessage();

            List<Mono<MessageAndContext>> each = new ArrayList<>();

            // Get payload
            Object payload = message.payload();
            if (payload instanceof Collection) {
                Collection<?> data = (Collection<?>) payload;
                for (Object item : data) {
                    createMonoFromItem(graph, firstEachNode, messageAndContext, each, item);
                }
            } else {
                // It is not a  collection, therefore it must be a single item.
                createMonoFromItem(graph, firstEachNode, messageAndContext, each, payload);
            }

            return Mono.zip(each, messagesCombinator())
                    .flatMap(eventsToJoin -> Mono.create(new JoinConsumer(messageAndContext, eventsToJoin, join)));
        });

        if (nextAfterStop == null) {
            // This is the last component of the flow, so we return the current publisher.
            return aggregatedForEachResults;
        }

        if (isJoinExecutionNode(nextAfterStop)) {
            // 'nextAfterStop' is an execution node referring to a join node which was executed
            // as a result of the JoinConsumer above. Therefore, the next node to be executed must be
            // the next after the Join Execution Node.
            return NextNode.of(nextAfterStop, graph).map(nextOfJoin ->
                    FlowExecutorFactory.get().execute(aggregatedForEachResults, nextOfJoin, graph))
                    .orElse(aggregatedForEachResults);
        } else {
            // If 'nextAfterStop' was NOT a join,
            // then we keep with the execution starting from 'nextAfterStop'.
            return FlowExecutorFactory.get().execute(aggregatedForEachResults, nextAfterStop, graph);
        }
    }

    private boolean isJoinExecutionNode(ExecutionNode nextAfterStop) {
        if (nextAfterStop != null) {
            Component joinComponent = nextAfterStop.getComponent();
            return joinComponent instanceof Join;
        }
        return false;
    }

    private void createMonoFromItem(ExecutionGraph graph,
                                    ExecutionNode firstEachNode,
                                    MessageAndContext messageAndContext,
                                    List<Mono<MessageAndContext>> each,
                                    Object item) {
        Message mes = MessageBuilder.get().withJavaObject(item).build();
        MessageAndContext eachMessageAndContext = messageAndContext.copyWithMessage(mes);
        Mono<MessageAndContext> parent = Mono.just(eachMessageAndContext);
        Publisher<MessageAndContext> eachPublisher = FlowExecutorFactory.get().execute(parent, firstEachNode, graph);
        each.add(Mono.from(eachPublisher));
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

    private Join joinComponentOrDefault(ExecutionNode nextAfterStop) {
        return Optional.ofNullable(nextAfterStop)
                .flatMap(executionNode -> executionNode.getComponent() instanceof Join ?
                        Optional.of((Join) executionNode.getComponent()) :
                        Optional.empty())
                .orElse(new DefaultJoin());
    }

    static class DefaultJoin implements Join {
        @Override
        public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {
            Map<String, Serializable> attributes = new HashMap<>();
            MessageAttributes emptyJoinAttributes = new DefaultMessageAttributes(DefaultJoin.class, attributes);

            List<Object> results = messagesToJoin.stream()
                    .map(Message::payload)
                    .collect(toList());
            return MessageBuilder.get()
                    .withJavaCollection(results, Object.class)
                    .attributes(emptyJoinAttributes)
                    .build();
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
