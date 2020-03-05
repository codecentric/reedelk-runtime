package com.reedelk.esb.component.foreach;

import com.reedelk.esb.commons.NextNode;
import com.reedelk.esb.component.commons.Combinator;
import com.reedelk.esb.component.commons.JoinConsumer;
import com.reedelk.esb.component.commons.JoinUtils;
import com.reedelk.esb.execution.FlowExecutor;
import com.reedelk.esb.execution.FlowExecutorFactory;
import com.reedelk.esb.execution.MessageAndContext;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.esb.services.scriptengine.ScriptEngine;
import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

        final Join join = JoinUtils.joinComponentOrDefault(nextAfterStop);

        Flux<MessageAndContext> forEachResults = Flux.from(publisher).flatMap(messageAndContext -> {

            FlowContext flowContext = messageAndContext.getFlowContext();

            Message message = messageAndContext.getMessage();

            Object payload =
                    ScriptEngine.getInstance().evaluate(collection, flowContext, message).orElse(null);

            List<Mono<MessageAndContext>> each = new ArrayList<>();

            // Get payload
            if (payload instanceof Collection) {
                Collection<?> data = (Collection<?>) payload;
                for (Object item : data) {
                    Mono<MessageAndContext> mono = monoFromCollectionItem(graph, firstEachNode, messageAndContext, item);
                    each.add(mono);
                }
            } else {
                // It is not a  collection, therefore it must be a single item.
                Mono<MessageAndContext> mono = monoFromCollectionItem(graph, firstEachNode, messageAndContext, payload);
                each.add(mono);
            }

            return Mono.zip(each, Combinator.messageAndContext())
                    .flatMap(eventsToJoin -> {
                        JoinConsumer joinConsumer = new JoinConsumer(messageAndContext, eventsToJoin, join);
                        return Mono.create(joinConsumer);
                    });
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

    private Mono<MessageAndContext> monoFromCollectionItem(ExecutionGraph graph,
                                                           ExecutionNode firstEachNode,
                                                           MessageAndContext messageAndContext,
                                                           Object item) {
        Message messageWithItem = item == null ?
                MessageBuilder.get().empty().build() :
                MessageBuilder.get().withJavaObject(item).build();
        MessageAndContext messageAndContextWithItem = messageAndContext.copyWithMessage(messageWithItem);
        Mono<MessageAndContext> parent = Mono.just(messageAndContextWithItem);
        Publisher<MessageAndContext> eachPublisher = FlowExecutorFactory.get().execute(parent, firstEachNode, graph);
        return Mono.from(eachPublisher);
    }
}
