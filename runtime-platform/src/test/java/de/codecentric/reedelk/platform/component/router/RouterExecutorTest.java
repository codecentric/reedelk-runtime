package de.codecentric.reedelk.platform.component.router;

import de.codecentric.reedelk.platform.execution.AbstractExecutionTest;
import de.codecentric.reedelk.platform.execution.MessageAndContext;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class RouterExecutorTest extends AbstractExecutionTest {

    private RouterExecutor executor = new RouterExecutor();

    private ExecutionNode routerNode;
    private ExecutionNode route1Node;
    private ExecutionNode route2Node;
    private ExecutionNode nodeFollowingStop;

    @BeforeEach
    void setUp() {
        routerNode = newExecutionNode(new RouterWrapper());
        route1Node = newExecutionNode(new AddPostfixSyncProcessor("-route1"));
        route2Node = newExecutionNode(new AddPostfixSyncProcessor("-route2"));
        nodeFollowingStop = newExecutionNode(new AddPostfixSyncProcessor("-following-stop"));
    }

    @Test
    void shouldExecuteCorrectBranchForGivenCondition() {
        // Given
        ExecutionGraph graph = RouterTestGraphBuilder.get()
                .router(routerNode)
                .inbound(inbound)
                .conditionWithSequence("#[message.payload() == 'Route1']", route1Node)
                .conditionWithSequence("#[message.payload() == 'Route2']", route2Node)
                .afterRouterSequence(nodeFollowingStop)
                .build();

        MessageAndContext event = newEventWithContent("Route2");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, routerNode, graph);

        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("Route2-route2-following-stop"))
                .verifyComplete();
    }

    @Test
    void shouldExecuteDefaultPath() {
        // Given
        ExecutionNode route3Node = newExecutionNode(new AddPostfixSyncProcessor("-otherwise"));
        ExecutionGraph graph = RouterTestGraphBuilder.get()
                .router(routerNode)
                .inbound(inbound)
                .conditionWithSequence("#[message.payload() == 'Route1']", route1Node)
                .conditionWithSequence("#[message.payload() == 'Route2']", route2Node)
                .conditionWithSequence("otherwise", route3Node)
                .afterRouterSequence(nodeFollowingStop)
                .build();

        MessageAndContext event = newEventWithContent("RouteOtherwise");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, routerNode, graph);

        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("RouteOtherwise-otherwise-following-stop"))
                .verifyComplete();
    }

    @Test
    void shouldExecuteUntilEndOfBranchWhenNoNodesAfterRouter() {
        // Given
        ExecutionGraph graph = RouterTestGraphBuilder.get()
                .router(routerNode)
                .inbound(inbound)
                .conditionWithSequence("#[message.payload() == 'Route1']", route1Node)
                .conditionWithSequence("#[message.payload() == 'Route2']", route2Node)
                .build();

        MessageAndContext event = newEventWithContent("Route1");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, routerNode, graph);

        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("Route1-route1"))
                .verifyComplete();
    }

    @Test
    void shouldExecuteCorrectBranchForAnyMessageInTheStream() {
        // Given
        ExecutionGraph graph = RouterTestGraphBuilder.get()
                .router(routerNode)
                .inbound(inbound)
                .conditionWithSequence("#[message.payload() == 'Route1']", route1Node)
                .conditionWithSequence("#[message.payload() == 'Route2']", route2Node)
                .afterRouterSequence(nodeFollowingStop)
                .build();

        MessageAndContext route2Event = newEventWithContent("Route2");
        MessageAndContext route1Event = newEventWithContent("Route1");

        Publisher<MessageAndContext> publisher = Flux.just(route2Event, route1Event);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, routerNode, graph);

        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("Route2-route2-following-stop"))
                .assertNext(assertMessageContains("Route1-route1-following-stop"))
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionAndStopExecutionWhenBranchProcessorThrowsException() {
        // Given
        String exceptionMessage = "Illegal state exception";
        ExecutionNode processorThrowingException = newExecutionNode(new ProcessorThrowingIllegalStateExceptionSync(exceptionMessage));

        ExecutionGraph graph = RouterTestGraphBuilder.get()
                .router(routerNode)
                .inbound(inbound)
                .conditionWithSequence("#[message.payload() == 'Route1']", processorThrowingException)
                .conditionWithSequence("#[message.payload() == 'Route2']", route2Node)
                .afterRouterSequence(nodeFollowingStop)
                .build();

        MessageAndContext event = newEventWithContent("Route1");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, routerNode, graph);

        StepVerifier.create(endPublisher)
                .verifyErrorMatches(throwable ->
                        throwable.getMessage().equals(exceptionMessage + " (Route1)") &&
                                throwable instanceof IllegalStateException);
    }
}
