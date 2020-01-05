package com.reedelk.esb.execution;

import com.reedelk.esb.component.RouterWrapper;
import com.reedelk.esb.component.RouterWrapper.PathExpressionPair;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.esb.services.scriptengine.ScriptEngine;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import com.reedelk.runtime.component.Router;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.reedelk.esb.execution.ExecutionUtils.nextNodeOf;
import static java.util.stream.Collectors.toList;

public class RouterExecutor implements FlowExecutor {

    private final Logger logger = LoggerFactory.getLogger(RouterExecutor.class);
    private final Mono<Boolean> unmatchedCondition = Mono.just(false);
    private final Mono<Boolean> matchedCondition = Mono.just(true);

    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {

        RouterWrapper router = (RouterWrapper) currentNode.getComponent();

        List<RouterWrapper.PathExpressionPair> pathExpressionPairs = router.getPathExpressionPairs();

        // Need to keep going and continue to execute the flow after the choice joins...
        Publisher<MessageAndContext> flux = Flux.from(publisher).flatMap(messageContext -> {

            // Create choice branches
            List<Mono<MessageAndContext>> choiceBranches = pathExpressionPairs.stream()
                    .map(pathExpressionPair -> createConditionalBranch(pathExpressionPair, messageContext, graph))
                    .collect(toList());

            // Create a flow with all conditional flows, only the flow evaluating to true will be executed.
            return Flux.concat(choiceBranches)
                    .take(1) // We just select the first one, in case there are more than one matching
                    .switchIfEmpty(subscriber -> // If there is no match, the default path is then executed
                            createDefaultMono(router.getDefaultPathOrThrow(), messageContext, graph)
                                    .subscribe(subscriber));
        });

        ExecutionNode stopNode = router.getEndOfRouterStopNode();

        // If the Router is followed by other nodes, then we keep executing
        // the other nodes, otherwise we stop and we return the current publisher.
        return nextNodeOf(stopNode, graph)
                .map(nodeAfterStop -> FlowExecutorFactory.get().execute(flux, nodeAfterStop, graph))
                .orElse(flux); // The Router is the last execution node of the flow.
    }

    private Mono<MessageAndContext> createDefaultMono(PathExpressionPair pair, MessageAndContext message, ExecutionGraph graph) {
        ExecutionNode defaultExecutionNode = pair.pathReference;
        Publisher<MessageAndContext> parent = Flux.just(message);
        return Mono.from(FlowExecutorFactory.get()
                .execute(parent, defaultExecutionNode, graph));
    }

    private Mono<MessageAndContext> createConditionalBranch(PathExpressionPair pair, MessageAndContext event, ExecutionGraph graph) {
        DynamicString expression = pair.expression;
        ExecutionNode pathExecutionNode = pair.pathReference;
        // This Mono evaluates the expression. If the expression is true,
        // then the branch subflow gets executed, otherwise the message is dropped
        // and the flow is not executed.
        Mono<MessageAndContext> parent = Mono.just(event)
                .filterWhen(value -> evaluate(expression, event.getMessage(), event.getFlowContext()));

        return Mono.from(FlowExecutorFactory.get().execute(parent, pathExecutionNode, graph));
    }

    private Mono<Boolean> evaluate(DynamicString expression, Message message, FlowContext flowContext) {
        try {
            return Router.DEFAULT_CONDITION.equals(expression) ?
                    matchedCondition :
                    ScriptEngine.getInstance().evaluate(expression, flowContext, message)
                            .map(resultAsString -> Mono.just(Boolean.parseBoolean(resultAsString)))
                            .orElse(unmatchedCondition);
        } catch (Exception e) {
            logger.error(String.format("Could not evaluate Router path expression (%s)", expression), e);
            return unmatchedCondition;
        }
    }
}