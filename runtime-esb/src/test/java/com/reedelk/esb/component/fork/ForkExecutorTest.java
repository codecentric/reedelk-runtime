package com.reedelk.esb.component.fork;

import com.reedelk.esb.execution.AbstractExecutionTest;
import com.reedelk.esb.execution.MessageAndContext;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class ForkExecutorTest extends AbstractExecutionTest {

    private ForkExecutor executor = spy(new ForkExecutor());

    private ExecutionNode forkNode;
    private ExecutionNode fork1Node;
    private ExecutionNode fork2Node;
    private ExecutionNode joinNode;
    private ExecutionNode nodeFollowingJoin;

    @BeforeEach
    void setUp() {
        ForkWrapper forkWrapper = spy(new ForkWrapper());
        doReturn(Schedulers.elastic()).when(executor).flowScheduler();

        forkNode = newExecutionNode(forkWrapper);
        joinNode = newExecutionNode(new JoinString());
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
                .verifyErrorMatches(throwable -> throwable instanceof ESBException);
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

    static class JoinString implements Join {
        @Override
        public Message apply(FlowContext flowContext, List<Message> messages) {
            String joined = messages.stream()
                    .map(message -> (String) message.content().data())
                    .collect(joining(","));
            return MessageBuilder.get().withText(joined).build();
        }
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
