package com.reedelk.platform.execution;

import com.reedelk.platform.execution.context.DefaultFlowContext;
import com.reedelk.platform.graph.ExecutionGraph;
import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.platform.test.utils.TestComponent;
import com.reedelk.runtime.api.exception.PlatformException;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


class ProcessorSyncExecutorTest extends AbstractExecutionTest {

    private ProcessorSyncExecutor executor = new ProcessorSyncExecutor();
    private ExecutionNode processor = newExecutionNode(new AddPostfixSyncProcessor("-postfix"));

    @Test
    void shouldCorrectlyApplyProcessorToMessage() {
        // Given
        ExecutionGraph graph = newGraphSequence(inbound, processor,     stop);
        MessageAndContext event = newEventWithContent("input");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, processor, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("input-postfix"))
                .verifyComplete();
    }

    @Test
    void shouldCorrectlyApplyProcessorToEachMessageInTheStream() {
        // Given
        ExecutionGraph graph = newGraphSequence(inbound, processor, stop);
        MessageAndContext event1 = newEventWithContent("input1");
        MessageAndContext event2 = newEventWithContent("input2");
        Publisher<MessageAndContext> publisher = Flux.just(event1, event2);

        // When
        Publisher<MessageAndContext> endPublisher =
                executor.execute(publisher, processor, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("input1-postfix"))
                .assertNext(assertMessageContains("input2-postfix"))
                .verifyComplete();
    }

    @Test
    void shouldCorrectlyThrowErrorWhenProcessorThrowsException() {
        // Given
        String exceptionThrown = "Illegal state error";
        ExecutionNode processor = newExecutionNode(new ProcessorThrowingIllegalStateExceptionSync(exceptionThrown));
        ExecutionGraph graph = newGraphSequence(inbound, processor, stop);
        Message message = MessageBuilder.get(TestComponent.class).withText("input").build();

        MessageAndContext inputMessageAndContext = new MessageAndContext(message, DefaultFlowContext.from(message));

        Publisher<MessageAndContext> publisher = Flux.just(inputMessageAndContext);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, processor, graph);

        // Then
        StepVerifier.create(endPublisher)
                .verifyErrorMatches(throwable -> throwable instanceof IllegalStateException &&
                        throwable.getMessage().equals(exceptionThrown + " (input)"));
    }

    @Test
    void shouldCorrectlyThrowErrorWhenProcessorThrowsNoClassDefFoundError() {
        // Given
        String missingClazz = "javax.xml";
        ExecutionNode processor = newExecutionNode(new ProcessorThrowingNoClassDefFoundErrorSync(missingClazz));
        ExecutionGraph graph = newGraphSequence(inbound, processor, stop);
        Message message = MessageBuilder.get(TestComponent.class).withText("input").build();

        MessageAndContext inputMessageAndContext = new MessageAndContext(message, DefaultFlowContext.from(message));

        Publisher<MessageAndContext> publisher = Flux.just(inputMessageAndContext);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, processor, graph);

        // Then: we make sure that the Throwable exception is wrapped
        // as ESBException and then we check the message.
        StepVerifier.create(endPublisher)
                .verifyErrorMatches(throwable -> throwable instanceof PlatformException &&
                       throwable.getMessage().equals("java.lang.NoClassDefFoundError: javax.xml"));
    }

    // If the processor is the last node, then it must be present a Stop node.
    // If a Stop node is not there, it means there has been an error while
    // building the graph.
    @Test
    void shouldThrowExceptionIfProcessorNotFollowedByAnyOtherNode() {
        // Given
        ExecutionNode processor = newExecutionNode(new AddPostfixSyncProcessor("exception"));
        ExecutionGraph graph = newGraphSequence(inbound, processor);

        // When
        Assertions.assertThrows(IllegalStateException.class, () ->
                        executor.execute(Flux.just(), processor, graph),
                "Expected processor sync to be followed by one node");
    }
}
