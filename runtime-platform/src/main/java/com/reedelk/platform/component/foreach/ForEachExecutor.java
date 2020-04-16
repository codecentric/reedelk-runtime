package com.reedelk.platform.component.foreach;

import com.reedelk.platform.commons.NextNode;
import com.reedelk.platform.component.commons.Combinator;
import com.reedelk.platform.component.commons.JoinConsumer;
import com.reedelk.platform.component.commons.JoinUtils;
import com.reedelk.platform.execution.FlowExecutor;
import com.reedelk.platform.execution.FlowExecutorFactory;
import com.reedelk.platform.execution.MessageAndContext;
import com.reedelk.platform.graph.ExecutionGraph;
import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.platform.services.scriptengine.ScriptEngine;
import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.Pair;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import com.reedelk.runtime.component.ForEach;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.reedelk.runtime.api.commons.Preconditions.checkArgument;

public class ForEachExecutor implements FlowExecutor {

    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {

        ForEachWrapper forEach = (ForEachWrapper) currentNode.getComponent();

        DynamicObject collection = forEach.getCollection();

        ExecutionNode firstEachNode = forEach.getFirstEachNode();

        ExecutionNode stopNode = forEach.getStopNode();

        // If the stop node does not have next node, then it means that the for each
        // is not followed by any other component.
        ExecutionNode nextAfterStop = NextNode.of(stopNode, graph).orElse(null);

        // If the for each does not contain any component, we have two cases:
        // 1. Exists a node after 'stop' node, then we need to execute the remaining flow outside the foreach.
        // 2. Does not exist a node after 'stop': we return the original stream.
        if (firstEachNode == null) {
            return nextAfterStop != null ?
                    FlowExecutorFactory.get().execute(publisher, nextAfterStop, graph) :
                    publisher;
        }

        final Join join = JoinUtils.joinComponentOrDefault(nextAfterStop);

        Flux<MessageAndContext> forEachResults = Flux.from(publisher).flatMap(messageAndContext -> {

            FlowContext flowContext = messageAndContext.getFlowContext();

            Message message = messageAndContext.getMessage();

            Object payload = ScriptEngine.getInstance()
                    .evaluate(collection, flowContext, message)
                    .orElse(null);

            List<Mono<MessageAndContext>> each =
                    createEachBranchesFromPayload(graph, firstEachNode, messageAndContext, payload);

            if (each.isEmpty()) {
                // In this case the input collection or map was empty and therefore there are no
                // branches in the each collection. We immediately return the join
                // consumer without zipping the each 'branches' since there are none.
                JoinConsumer joinConsumer = new JoinConsumer(messageAndContext, new MessageAndContext[0], join);
                return Mono.create(joinConsumer);

            } else {
                return Mono.zip(each, Combinator.messageAndContext())
                        .flatMap(eventsToJoin -> {
                            JoinConsumer joinConsumer = new JoinConsumer(messageAndContext, eventsToJoin, join);
                            return Mono.create(joinConsumer);
                        });
            }
        });

        if (nextAfterStop == null) {
            // This is the last component of the flow, so we return the current publisher.
            return forEachResults;
        }

        if (JoinUtils.isJoin(nextAfterStop)) {
            // 'nextAfterStop' is an execution node referring to a join node which was executed
            // as a result of the JoinConsumer above. Therefore, the next node to be executed must be
            // the next after the Join Execution Node.
            return NextNode.of(nextAfterStop, graph).map(nextOfJoin ->
                    FlowExecutorFactory.get().execute(forEachResults, nextOfJoin, graph))
                    .orElse(forEachResults);
        } else {
            // If 'nextAfterStop' was NOT a join,
            // then we keep with the execution starting from 'nextAfterStop'.
            return FlowExecutorFactory.get().execute(forEachResults, nextAfterStop, graph);
        }
    }

    private List<Mono<MessageAndContext>> createEachBranchesFromPayload(ExecutionGraph graph, ExecutionNode firstEachNode, MessageAndContext messageAndContext, Object payload) {
        List<Mono<MessageAndContext>> each = new ArrayList<>();
        if (payload instanceof Map) {
            Map<?,?> payloadMap = (Map<?, ?>) payload;
            for (Map.Entry<?,?> entry : payloadMap.entrySet()) {
                Serializable key = checkSerializableOrThrow(entry.getKey(), "Map key");
                Serializable value = checkSerializableOrThrow(entry.getValue(), "Map value");
                Pair<Serializable,Serializable> realEntry = Pair.create(key, value);
                Mono<MessageAndContext> mono = monoWithItem(graph, firstEachNode, messageAndContext, realEntry);
                each.add(mono);
            }
        } else if (payload instanceof Collection) {
            Collection<?> data = (Collection<?>) payload;
            for (Object item : data) {
                Mono<MessageAndContext> mono = monoWithItem(graph, firstEachNode, messageAndContext, item);
                each.add(mono);
            }
        } else {
            // It is not a  collection, therefore it must be a single item.
            Mono<MessageAndContext> mono = monoWithItem(graph, firstEachNode, messageAndContext, payload);
            each.add(mono);
        }
        return each;
    }

    private Mono<MessageAndContext> monoWithItem(ExecutionGraph graph,
                                                 ExecutionNode firstEachNode,
                                                 MessageAndContext messageAndContext,
                                                 Object item) {
        Message messageWithItem = item == null ?
                MessageBuilder.get(ForEach.class).empty().build() :
                MessageBuilder.get(ForEach.class).withJavaObject(item).build();
        MessageAndContext messageAndContextWithItem = messageAndContext.copyWithMessage(messageWithItem);
        Mono<MessageAndContext> parent = Mono.just(messageAndContextWithItem);
        Publisher<MessageAndContext> eachPublisher = FlowExecutorFactory.get().execute(parent, firstEachNode, graph);
        return Mono.from(eachPublisher);
    }

    private static Serializable checkSerializableOrThrow(Object value, String message) {
        checkArgument(value == null || value instanceof Serializable,
                "ForEach Component " + message + " must be serializable.");
        return (Serializable) value;
    }
}
