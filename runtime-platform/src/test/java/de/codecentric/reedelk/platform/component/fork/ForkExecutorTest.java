package de.codecentric.reedelk.platform.component.fork;

import de.codecentric.reedelk.platform.execution.AbstractExecutionTest;
import de.codecentric.reedelk.platform.execution.MessageAndContext;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.api.component.Join;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class ForkExecutorTest extends AbstractExecutionTest {

    private ForkExecutor executor = Mockito.spy(new ForkExecutor());

    private ExecutionNode forkNode;
    private ExecutionNode fork1Node;
    private ExecutionNode fork2Node;
    private ExecutionNode joinNode;
    private ExecutionNode nodeFollowingJoin;

    @BeforeEach
    void setUp() {
        ForkWrapper forkWrapper = Mockito.spy(new ForkWrapper());
        doReturn(Schedulers.elastic()).when(executor).flowScheduler();

        forkNode = newExecutionNode(forkWrapper);
        joinNode = newExecutionNode(new JoinStringWithDelimiter(","));
        fork1Node = newExecutionNode(new AddPostfixSyncProcessor("-fork1"));
        fork2Node = newExecutionNode(new AddPostfixSyncProcessor("-fork2"));
        nodeFollowingJoin = newExecutionNode(new AddPostfixSyncProcessor("-following-join"));
    }

    @Test
    void shouldForkAndJoinCorrectlyThePayload() {
        // Given
        ExecutionGraph graph = ForkTestGraphBuilder.get()
                .fork(forkNode)
                .inbound(inbound)
                .forkSequence(fork1Node)
                .forkSequence(fork2Node)
                .join(joinNode)
                .build();

        MessageAndContext event = newEventWithContent("ForkTest");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forkNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("ForkTest-fork1,ForkTest-fork2"))
                .verifyComplete();
    }

    @Test
    void shouldForkAndJoinCorrectlyForAnyMessageInTheStream() {
        // Given
        ExecutionGraph graph = ForkTestGraphBuilder.get()
                .fork(forkNode)
                .inbound(inbound)
                .forkSequence(fork1Node)
                .forkSequence(fork2Node)
                .join(joinNode)
                .build();

        MessageAndContext event1 = newEventWithContent("ForkTest1");
        MessageAndContext event2 = newEventWithContent("ForkTest2");
        Publisher<MessageAndContext> publisher = Flux.just(event1, event2);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forkNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContainsOneOf("ForkTest1-fork1,ForkTest1-fork2", "ForkTest2-fork1,ForkTest2-fork2"))
                .assertNext(assertMessageContainsOneOf("ForkTest1-fork1,ForkTest1-fork2", "ForkTest2-fork1,ForkTest2-fork2"))
                .verifyComplete();
    }

    @Test
    void shouldForkAndJoinCorrectlyAndContinueExecutionUntilTheEndOfTheGraph() {
        // Given
        ExecutionGraph graph = ForkTestGraphBuilder.get()
                .fork(forkNode)
                .inbound(inbound)
                .forkSequence(fork1Node)
                .forkSequence(fork2Node)
                .join(joinNode)
                .afterForkSequence(nodeFollowingJoin)
                .build();

        MessageAndContext event = newEventWithContent("ForkTest");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forkNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("ForkTest-fork1,ForkTest-fork2-following-join"))
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionAndStopExecutionWhenBranchProcessorThrowsException() {
        // Given
        String exceptionMessage = "ForkException thrown";
        ExecutionNode processorThrowingException = newExecutionNode(new ProcessorThrowingIllegalStateExceptionSync(exceptionMessage));

        ExecutionGraph graph = ForkTestGraphBuilder.get()
                .fork(forkNode)
                .inbound(inbound)
                .forkSequence(fork1Node)
                .forkSequence(processorThrowingException)
                .join(joinNode)
                .afterForkSequence(nodeFollowingJoin)
                .build();

        MessageAndContext event = newEventWithContent("ForkTest");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, forkNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .verifyErrorMatches(throwable ->
                        throwable instanceof IllegalStateException &&
                                throwable.getMessage().equals(exceptionMessage + " (ForkTest)"));
    }

    @Test
    void shouldThrowExceptionAndStopExecutionWhenJoinProcessorThrowsException() {
        // Given
        ExecutionNode joinThrowingException = newExecutionNode(new JoinThrowingException());

        ExecutionGraph graph = ForkTestGraphBuilder.get()
                .fork(forkNode)
                .inbound(inbound)
                .forkSequence(fork1Node)
                .forkSequence(fork2Node)
                .join(joinThrowingException)
                .afterForkSequence(nodeFollowingJoin)
                .build();

        MessageAndContext event = newEventWithContent("ForkTest");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, forkNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .verifyErrorMatches(throwable -> throwable instanceof IllegalStateException);
    }

    @Test
    void shouldThrowExceptionAndStopExecutionWhenJoinProcessorThrowsThrowable() {
        // Given
        ExecutionNode joinThrowingNoClassDefFound = newExecutionNode(new JoinThrowingNoClassDefFoundError());

        ExecutionGraph graph = ForkTestGraphBuilder.get()
                .fork(forkNode)
                .inbound(inbound)
                .forkSequence(fork1Node)
                .forkSequence(fork2Node)
                .join(joinThrowingNoClassDefFound)
                .afterForkSequence(nodeFollowingJoin)
                .build();

        MessageAndContext event = newEventWithContent("ForkTest");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, forkNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .verifyErrorMatches(throwable -> throwable instanceof PlatformException);
    }

    @Test
    void shouldStopExecutionWhenForkNotFollowedByAnyOtherNode() {
        // Given
        ExecutionGraph graph = ForkTestGraphBuilder.get()
                .fork(forkNode)
                .inbound(inbound)
                .forkSequence(fork1Node)
                .forkSequence(fork2Node)
                .build();

        MessageAndContext event = newEventWithContent("ForkTest");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forkNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContainsInAnyOrder("ForkTest-fork1", "ForkTest-fork2"))
                .verifyComplete();
    }

    @Test
    void shouldConsumeContentBeforeExecutingForkBranchesWhenStream() {
        // Given
        ExecutionNode toStringExecutionNode1 = newExecutionNode(new ToStringProcessor());
        ExecutionNode toStringExecutionNode2 = newExecutionNode(new ToStringProcessor());
        ExecutionGraph graph = ForkTestGraphBuilder.get()
                .fork(forkNode)
                .inbound(inbound)
                .forkSequence(toStringExecutionNode1, fork1Node) // List to string
                .forkSequence(toStringExecutionNode2, fork2Node) // List to string
                .join(joinNode)
                .build();

        MessageAndContext event = newEventWithContent(Flux.just("one", "two", "three"));
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forkNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("[one, two, three]-fork1,[one, two, three]-fork2"))
                .verifyComplete();
    }

    static class JoinThrowingException implements Join {
        @Override
        public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {
            throw new IllegalStateException("Join not valid");
        }
    }

    static class JoinThrowingNoClassDefFoundError implements Join {
        @Override
        public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {
            throw new NoClassDefFoundError("javax.xml");
        }
    }
}
