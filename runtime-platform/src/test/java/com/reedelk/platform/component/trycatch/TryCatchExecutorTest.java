package com.reedelk.platform.component.trycatch;

import com.reedelk.platform.execution.AbstractExecutionTest;
import com.reedelk.platform.execution.MessageAndContext;
import com.reedelk.platform.graph.ExecutionGraph;
import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.platform.test.utils.TestComponent;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.spy;

class TryCatchExecutorTest extends AbstractExecutionTest {

    private final String exceptionMessage = "TryCatch-Exception thrown";

    private TryCatchExecutor executor = new TryCatchExecutor();

    private ExecutionNode tryNode;
    private ExecutionNode catchNode;
    private ExecutionNode catchWithException;

    private ExecutionNode tryCatchNode;
    private ExecutionNode tryWithException;

    @BeforeEach
    void setUp() {
        TryCatchWrapper tryCatchWrapper = spy(new TryCatchWrapper());
        tryCatchNode = newExecutionNode(tryCatchWrapper);
        tryNode = newExecutionNode(new AddPostfixSyncProcessor("-try"));
        catchNode = newExecutionNode(new CatchSyncProcessor());
        tryWithException = newExecutionNode(new ProcessorThrowingIllegalStateExceptionSync(exceptionMessage));
        catchWithException = newExecutionNode(new ProcessorThrowingIllegalStateExceptionSync(exceptionMessage));
    }

    @Test
    void shouldExecuteTryFlow() {
        // Given
        ExecutionGraph graph = TryCatchTestGraphBuilder.get()
                .inbound(inbound)
                .tryNodes(tryNode)
                .catchNodes(catchNode)
                .tryCatchNode(tryCatchNode)
                .build();

        MessageAndContext event = newEventWithContent("TryCatchTest");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, tryCatchNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("TryCatchTest-try"))
                .verifyComplete();
    }

    @Test
    void shouldExecuteCatchFlow() {
        // Given
        ExecutionGraph graph = TryCatchTestGraphBuilder.get()
                .inbound(inbound)
                .catchNodes(catchNode)
                .tryNodes(tryWithException)
                .tryCatchNode(tryCatchNode)
                .build();

        MessageAndContext event = newEventWithContent("input message");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, tryCatchNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains(exceptionMessage + " (input message)"))
                .verifyComplete();
    }

    @Test
    void shouldContinueFlowExecutionAfterTryCatchAndExceptionCaught() {
        // Given
        ExecutionNode afterTryCatchNode = newExecutionNode(new AddPostfixSyncProcessor("-afterTryCatchNode"));


        ExecutionGraph graph = TryCatchTestGraphBuilder.get()
                .inbound(inbound)
                .catchNodes(catchNode)
                .tryNodes(tryWithException)
                .tryCatchNode(tryCatchNode)
                .afterTryCatchSequence(afterTryCatchNode)
                .build();

        MessageAndContext event = newEventWithContent("input message");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, tryCatchNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains(exceptionMessage + " (input message)-afterTryCatchNode"))
                .verifyComplete();
    }

    @Test
    void shouldExecuteFlowFollowingForkNodeAfterCatchFlow() {
        // Given
        ExecutionNode afterTryCatchNode = newExecutionNode(new AddPostfixSyncProcessor("-afterTryCatchNode"));
        ExecutionGraph graph = TryCatchTestGraphBuilder.get()
                .inbound(inbound)
                .catchNodes(catchNode)
                .tryNodes(tryWithException)
                .tryCatchNode(tryCatchNode)
                .afterTryCatchSequence(afterTryCatchNode)
                .build();

        MessageAndContext event = newEventWithContent("input message");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, tryCatchNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains(exceptionMessage + " (input message)-afterTryCatchNode"))
                .verifyComplete();
    }

    @Test
    void shouldRethrowExceptionWhenExceptionThrownInsideCatchFlow() {
        // Given
        ExecutionNode afterTryCatchNode = newExecutionNode(new AddPostfixSyncProcessor("-afterTryCatchNode"));
        ExecutionGraph graph = TryCatchTestGraphBuilder.get()
                .inbound(inbound)
                .catchNodes(catchNode, catchWithException)
                .tryNodes(tryWithException)
                .tryCatchNode(tryCatchNode)
                .afterTryCatchSequence(afterTryCatchNode)
                .build();

        MessageAndContext event = newEventWithContent("input message");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, tryCatchNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .expectErrorMatches(throwable -> throwable instanceof IllegalStateException &&
                        throwable.getMessage().equals("TryCatch-Exception thrown (TryCatch-Exception thrown (input message))"))
                .verify();
    }

    @Test
    void shouldExecuteCatchFlowWhenExceptionThrownAfterFirstTryNode() {
        // Given
        ExecutionGraph graph = TryCatchTestGraphBuilder.get()
                .inbound(inbound)
                .catchNodes(catchNode)
                .tryNodes(tryNode, tryWithException)
                .tryCatchNode(tryCatchNode)
                .build();

        MessageAndContext event = newEventWithContent("input message");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, tryCatchNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains(exceptionMessage + " (input message-try)"))
                .verifyComplete();
    }

    @Test
    void shouldExecuteProcessorsBeforeTryCatchAtMostOneTimeWhenExceptionOccurred() {
        ExecutionGraph graph = TryCatchTestGraphBuilder.get()
                .inbound(inbound)
                .catchNodes(catchNode)
                .tryNodes(tryNode, tryWithException)
                .tryCatchNode(tryCatchNode)
                .build();

        AtomicInteger numberOfExecutions = new AtomicInteger(0);

        MessageAndContext event = newEventWithContent("input message");
        Publisher<MessageAndContext> publisher = Mono.just(event).handle((messageAndContext, sink) -> {
            Message message = messageAndContext.getMessage();
            String content = message.payload();
            Message newMessage = MessageBuilder.get(TestComponent.class).withText(content + "-handler").build();
            messageAndContext.replaceWith(newMessage);
            sink.next(messageAndContext);
            numberOfExecutions.incrementAndGet();
        });

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, tryCatchNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("TryCatch-Exception thrown (input message-handler-try)"))
                .verifyComplete();

        Assertions.assertThat(numberOfExecutions.get()).isEqualTo(1);
    }

    static class CatchSyncProcessor implements ProcessorSync {
        @Override
        public Message apply(FlowContext flowContext, Message message) {
            Exception thrown = (Exception) message.content().data();
            String outputString = thrown.getMessage();
            return MessageBuilder.get(TestComponent.class).withText(outputString).build();
        }
    }
}
