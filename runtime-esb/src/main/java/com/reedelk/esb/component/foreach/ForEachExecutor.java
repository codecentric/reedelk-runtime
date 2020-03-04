package com.reedelk.esb.component.foreach;

import com.reedelk.esb.commons.NextNode;
import com.reedelk.esb.execution.FlowExecutor;
import com.reedelk.esb.execution.FlowExecutorFactory;
import com.reedelk.esb.execution.MessageAndContext;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.ObjectCollectionContent;
import com.reedelk.runtime.api.message.content.TypedContent;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class ForEachExecutor implements FlowExecutor {

    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {

        ForEachWrapper forEach = (ForEachWrapper) currentNode.getComponent();

        ExecutionNode firstEachNode = forEach.getFirstEachNode();

        ExecutionNode stopNode = forEach.getStopNode();

        // If the stop node does not have next node, then it means that the for each
        // is not followed by any other component.
        ExecutionNode nextAfterStop = NextNode.of(stopNode, graph).orElse(null);

        // Join strategy

        Flux<MessageAndContext> aggregatedForEachResults = Flux.from(publisher).flatMap(messageAndContext -> {
            Message message = messageAndContext.getMessage();
            TypedContent<?, ?> content = message.getContent();

            List<Mono<MessageAndContext>> each = new ArrayList<>();
            if (content instanceof ObjectCollectionContent) {
                ObjectCollectionContent<?> collection = (ObjectCollectionContent<?>) content;
                Collection<?> data = collection.data();
                for (Object item : data) {
                    Message mes = MessageBuilder.get().withJavaObject(item).build();
                    MessageAndContext eachMessageAndContext = messageAndContext.copyWithMessage(mes);
                    Mono<MessageAndContext> parent = Mono.just(eachMessageAndContext);
                    Publisher<MessageAndContext> eachPublisher = FlowExecutorFactory.get().execute(parent, firstEachNode, graph);
                    each.add(Mono.from(eachPublisher));
                }
            } else {
                // Get payload
                Object payload = message.payload();
                if (payload instanceof Collection) {
                    Collection<?> data = (Collection<?>) payload;
                    for (Object item : data) {
                        Message mes = MessageBuilder.get().withJavaObject(item).build();
                        MessageAndContext eachMessageAndContext = messageAndContext.copyWithMessage(mes);
                        Mono<MessageAndContext> parent = Mono.just(eachMessageAndContext);
                        Publisher<MessageAndContext> eachPublisher = FlowExecutorFactory.get().execute(parent, firstEachNode, graph);
                        each.add(Mono.from(eachPublisher));
                    }
                } else {
                    // it is not a collection, single item.
                    Message mes = MessageBuilder.get().withJavaObject(payload).build();
                    MessageAndContext eachMessageAndContext = messageAndContext.copyWithMessage(mes);
                    Mono<MessageAndContext> parent = Mono.just(eachMessageAndContext);
                    Publisher<MessageAndContext> eachPublisher = FlowExecutorFactory.get().execute(parent, firstEachNode, graph);
                    each.add(Mono.from(eachPublisher));
                }
            }

            return Mono.zip(each, messagesCombinator(messageAndContext));
        });

        // TODO: Add check for node after foreach.

        return aggregatedForEachResults;
    }

    private Function<Object[], MessageAndContext> messagesCombinator(MessageAndContext mAndC) {
        return objects -> {
            List<Object> payloads = new ArrayList<>();
            for (Object object : objects) {
                MessageAndContext messageAndContext = (MessageAndContext) object;
                Object payload = messageAndContext.getMessage().payload();
                payloads.add(payload);
            }
            mAndC.replaceWith(MessageBuilder.get().withJavaObject(payloads).build());
            return mAndC;
        };
    }
}
