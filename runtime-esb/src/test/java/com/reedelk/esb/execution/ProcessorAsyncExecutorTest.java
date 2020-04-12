package com.reedelk.esb.execution;

import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.component.ProcessorAsync;
import com.reedelk.runtime.api.exception.PlatformException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@MockitoSettings(strictness = Strictness.LENIENT)
class ProcessorAsyncExecutorTest extends AbstractExecutionTest {

    private ProcessorAsyncExecutor executor = spy(new ProcessorAsyncExecutor());

    @BeforeEach
    void setUp() {
        doReturn(Schedulers.elastic()).when(executor).flowScheduler();
        doReturn(Optional.of(500L)).when(executor).asyncCallbackTimeout();
    }

    @Test
    void shouldCorrectlyApplyProcessorAsyncToMessage() {
        // Given
        ExecutionNode processor = newExecutionNode(new AddPostfixAsync("-async"));

        ExecutionGraph graph = newGraphSequence(inbound, processor, stop);

        MessageAndContext event = newEventWithContent("input");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, processor, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("input-async"))
                .verifyComplete();
    }

    @Test
    void shouldCorrectlyApplyProcessorAsyncToEachMessageInTheStream() {
        // Given
        ExecutionNode processor = newExecutionNode(new AddPostfixAsync("-async"));

        ExecutionGraph graph = newGraphSequence(inbound, processor, stop);
        MessageAndContext event1 = newEventWithContent("input1");
        MessageAndContext event2 = newEventWithContent("input2");
        Publisher<MessageAndContext> publisher = Flux.just(event1, event2);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, processor, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContainsOneOf("input1-async", "input2-async"))
                .assertNext(assertMessageContainsOneOf("input1-async", "input2-async"))
                .verifyComplete();
    }

    @Test
    void shouldCorrectlyThrowErrorWhenProcessorAsyncThrowsException() {
        // Given
        ExecutionNode processor = newExecutionNode(new ProcessorThrowingExceptionAsync());
        ExecutionGraph graph = newGraphSequence(inbound, processor, stop);
        MessageAndContext inputMessageAndContext = newEventWithContent("input");

        Publisher<MessageAndContext> publisher = Flux.just(inputMessageAndContext);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, processor, graph);

        // Then
        StepVerifier.create(endPublisher)
                .verifyErrorMatches(throwable -> throwable instanceof IllegalStateException);
    }

    @Test
    void shouldCorrectlyThrowErrorWhenProcessorAsyncThrowsThrowable() {
        // Given
        ExecutionNode processor = newExecutionNode(new ProcessorThrowingThrowableAsync());
        ExecutionGraph graph = newGraphSequence(inbound, processor, stop);
        MessageAndContext inputMessageAndContext = newEventWithContent("input");

        Publisher<MessageAndContext> publisher = Flux.just(inputMessageAndContext);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, processor, graph);

        // Then
        StepVerifier.create(endPublisher)
                .verifyErrorMatches(throwable -> {
                    // We must make sure that a Throwable not extending 'Exception' is correctly wrapped, so that
                    // we can correctly bubble up the error and expose the correct error to the user.
                    return throwable instanceof PlatformException &&
                            throwable.getMessage().equals("java.lang.NoClassDefFoundError: javax.xml");
                });
    }

    @Test
    void shouldCorrectlyThrowTimeoutErrorWhenProcessorAsyncWaitsTooLong() {
        // Given
        ExecutionNode processor = newExecutionNode(new ProcessorAsyncTakingTooLong());
        ExecutionGraph graph = newGraphSequence(inbound, processor, stop);
        MessageAndContext inputMessageAndContext = newEventWithContent("input");

        Publisher<MessageAndContext> publisher = Flux.just(inputMessageAndContext);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, processor, graph);

        // Then
        StepVerifier.create(endPublisher)
                .verifyErrorMatches(throwable -> throwable instanceof TimeoutException);
    }

    // If the processor is the last node, then it must be present a Stop node.
    // If a Stop node is not there, it means there has been an error while
    // building the graph.
    @Test
    void shouldThrowExceptionIfProcessorAsyncNotFollowedByAnyOtherNode() {
        // Given
        ExecutionNode processor = newExecutionNode(new AddPostfixAsync("-async"));
        ExecutionGraph graph = newGraphSequence(inbound, processor);

        // When
        Assertions.assertThrows(IllegalStateException.class, () ->
                        executor.execute(Flux.just(), processor, graph),
                "Expected processor sync to be followed by one node");
    }

    static class ProcessorAsyncTakingTooLong implements ProcessorAsync {
        @Override
        public void apply(FlowContext flowContext, Message input, OnResult callback) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // nothing to do
                }
                callback.onResult(flowContext, MessageBuilder.get().withText("hello").build());
            });
        }
    }

    static class ProcessorThrowingExceptionAsync implements ProcessorAsync {
        @Override
        public void apply(FlowContext flowContext, Message input, OnResult callback) {
            new Thread(() -> callback.onError(flowContext, new IllegalStateException("Error"))).start();
        }
    }

    static class ProcessorThrowingThrowableAsync implements ProcessorAsync {
        @Override
        public void apply(FlowContext flowContext, Message input, OnResult callback) {
            throw new NoClassDefFoundError("javax.xml");
        }
    }

    static class AddPostfixAsync implements ProcessorAsync {

        private String postfix;

        AddPostfixAsync(String postfix) {
            this.postfix = postfix;
        }

        @Override
        public void apply(FlowContext flowContext, Message input, OnResult callback) {
            new Thread(() -> {
                String inputString = (String) input.content().data();
                String outputString = inputString + postfix;
                Message out = MessageBuilder.get().withText(outputString).build();
                callback.onResult(flowContext, out);
            }).start();
        }
    }
}
