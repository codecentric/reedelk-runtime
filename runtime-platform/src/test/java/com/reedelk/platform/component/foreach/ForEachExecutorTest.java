package com.reedelk.platform.component.foreach;

import com.reedelk.platform.execution.AbstractExecutionTest;
import com.reedelk.platform.execution.MessageAndContext;
import com.reedelk.platform.graph.ExecutionGraph;
import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.runtime.api.commons.ImmutableMap;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

class ForEachExecutorTest extends AbstractExecutionTest {

    private ModuleContext context = new ModuleContext(1L);

    private ForEachExecutor executor = spy(new ForEachExecutor());

    private ExecutionNode forEachNode;
    private ExecutionNode eachNode1;
    private ExecutionNode eachNode2;

    @BeforeEach
    void setUp() {
        ForEachWrapper forEachWrapper = spy(new ForEachWrapper());
        forEachWrapper.setCollection(DynamicObject.from("#[message.payload()]", context));

        forEachNode = newExecutionNode(forEachWrapper);
        eachNode1 = newExecutionNode(new AddPostfixSyncProcessor("-each1"));
        eachNode2 = newExecutionNode(new AddPostfixSyncProcessor("-each2"));
    }

    @Test
    void shouldExecuteEachSequenceToEachItemInTheCollection() {
        // Given
        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .forEach(forEachNode)
                .forEachSequence(eachNode1, eachNode2)
                .build();

        MessageAndContext event = newEventWithContent(asList("one", "two"));
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContainsInAnyOrder("one-each1-each2", "two-each1-each2"))
                .verifyComplete();
    }

    @Test
    void shouldExecuteEachSequenceOnlyOnceWhenNoCollectionIsProvided() {
        // Given
        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .forEach(forEachNode)
                .forEachSequence(eachNode1, eachNode2)
                .build();

        MessageAndContext event = newEventWithContent("one");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContainsItems("one-each1-each2"))
                .verifyComplete();
    }

    @Test
    void shouldApplyJoin() {
        // Given
        ExecutionNode joinNode = newExecutionNode(new JoinStringWithDelimiter(":"));

        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .join(joinNode)
                .inbound(inbound)
                .forEach(forEachNode)
                .forEachSequence(eachNode1, eachNode2)
                .build();

        MessageAndContext event = newEventWithContent(asList("one", "two"));
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("one-each1-each2:two-each1-each2"))
                .verifyComplete();
    }

    @Test
    void shouldExecuteFlowComponentsAfterForEach() {
        // Given
        ExecutionNode joinNode = newExecutionNode(new JoinStringWithDelimiter("|"));
        ExecutionNode afterForEach = newExecutionNode(new AddPostfixSyncProcessor(" after for each"));

        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .join(joinNode)
                .forEach(forEachNode)
                .forEachSequence(eachNode1, eachNode2)
                .afterForEachSequence(afterForEach)
                .build();

        MessageAndContext event = newEventWithContent(asList("one", "two"));
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("one-each1-each2|two-each1-each2 after for each"))
                .verifyComplete();
    }

    @Test
    void shouldResolveApplyForEachToEachElementOfTheStream() {
        // Given
        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .forEach(forEachNode)
                .forEachSequence(eachNode1, eachNode2)
                .build();

        MessageAndContext event = newEventWithContent(TypedPublisher.fromString(Flux.just("one", "two")));
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContainsItems("onetwo-each1-each2"))
                .verifyComplete();
    }

    @Test
    void shouldApplyForEachFromScriptEvaluation() {
        // Given
        ForEachWrapper forEachWrapper = spy(new ForEachWrapper());
        forEachWrapper.setCollection(DynamicObject.from("#[['one', 'two', 'three', 'four']]", context));

        ExecutionNode forEachNode = newExecutionNode(forEachWrapper);

        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .forEach(forEachNode)
                .forEachSequence(eachNode1, eachNode2)
                .build();

        MessageAndContext event = newEventWithContent("Should be ignored");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContainsItems("one-each1-each2", "two-each1-each2", "three-each1-each2", "four-each1-each2"))
                .verifyComplete();
    }

    @Test
    void shouldReturnOriginalPayloadWhenForEachEmptyAndNoComponentsAfterwards() {
        // Given
        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .forEach(forEachNode)
                .build();

        TypedPublisher<String> payload = TypedPublisher.fromString(Flux.just("one", "two"));
        MessageAndContext event = newEventWithContent(payload);
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("onetwo"))
                .verifyComplete();
    }

    @Test
    void shouldReturnOriginalPayloadWhenForEachEmptyAndComponentsAfterwards() {
        // Given
        ExecutionNode afterForEach = newExecutionNode(new AddPostfixSyncProcessor(" after for each"));

        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .forEach(forEachNode)
                .afterForEachSequence(afterForEach)
                .build();

        TypedPublisher<String> payload = TypedPublisher.fromString(Flux.just("one", "two"));
        MessageAndContext event = newEventWithContent(payload);
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("onetwo after for each"))
                .verifyComplete();
    }

    @Test
    void shouldDoNothingListContentIsEmpty() {
        // Given
        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .forEach(forEachNode)
                .forEachSequence(eachNode1)
                .build();

        List<String> inputList = Collections.emptyList();
        // Test with message : withJavaObject 'ObjectContent'
        MessageAndContext event = newEventWithContent(inputList);
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(messageAndContext -> {
                    Message message = messageAndContext.getMessage();
                    List<?> outputCollection = message.payload();
                    assertThat(outputCollection).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void shouldDoNothingJavaCollectionWhenCollectionIsEmpty() {
        // Given
        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .forEach(forEachNode)
                .forEachSequence(eachNode1)
                .build();

        List<String> inputList = Collections.emptyList();
        // Test with message : withList 'ListContent'
        MessageAndContext event = newEventWithContent(inputList, String.class);
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(messageAndContext -> {
                    Message message = messageAndContext.getMessage();
                    List<?> outputCollection = message.payload();
                    assertThat(outputCollection).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void shouldDoNothingWhenListIsEmptyAndJoinNode() {
        // Given
        ExecutionNode joinNode = newExecutionNode(new JoinStringWithDelimiter("|"));

        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .join(joinNode)
                .inbound(inbound)
                .forEach(forEachNode)
                .forEachSequence(eachNode1)
                .build();

        List<String> list = Collections.emptyList();
        MessageAndContext event = newEventWithContent(list, String.class);
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                // Expect: Empty String Join result (no items where in the list)
                .assertNext(assertMessageContains(""))
                .verifyComplete();
    }

    @Test
    void shouldApplyForEachToEachItemOfMap() {
        // Given
        ExecutionNode joinNode = newExecutionNode(new JoinStringWithDelimiter("|"));
        ExecutionNode afterForEach = newExecutionNode(new AddPostfixSyncProcessor(" after for each"));
        ExecutionNode toStringExecutionNode = newExecutionNode(new ToStringProcessor());

        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .join(joinNode)
                .forEach(forEachNode)
                .afterForEachSequence(afterForEach)
                .forEachSequence(toStringExecutionNode, eachNode2)
                .build();

        MessageAndContext event = newEventWithContent(
                ImmutableMap.of(
                        "key1", asList("Item11", "Item12"),
                        "key2", asList("Item21", "Item22")));
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains("Pair{key=key1, value=[Item11, Item12]}-each2|Pair{key=key2, value=[Item21, Item22]}-each2 after for each"))
                .verifyComplete();
    }

    @Test
    void shouldDoNothingWhenMapIsEmptyAndJoinNode() {
        // Given
        ExecutionNode joinNode = newExecutionNode(new JoinStringWithDelimiter("|"));

        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .join(joinNode)
                .inbound(inbound)
                .forEach(forEachNode)
                .forEachSequence(eachNode1)
                .build();


        MessageAndContext event = newEventWithContent(ImmutableMap.of());
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                // Expect: Empty String Join result (no items where in the list)
                .assertNext(assertMessageContains(""))
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenMapContainsNotSerializableKey() {
        // Given
        ExecutionNode toStringExecutionNode = newExecutionNode(new ToStringProcessor());

        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .forEach(forEachNode)
                .forEachSequence(toStringExecutionNode, eachNode2)
                .build();

        MessageAndContext event = newEventWithContent(
                ImmutableMap.of(
                        new NotSerializableObject(), asList("Item11", "Item12"),
                        new NotSerializableObject(), asList("Item21", "Item22")));
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .verifyErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                "ForEach Component Map key must be serializable.".equals(throwable.getMessage()));
    }

    @Test
    void shouldThrowExceptionWhenMapContainsNotSerializableValue() {
        // Given
        ExecutionNode toStringExecutionNode = newExecutionNode(new ToStringProcessor());

        ExecutionGraph graph = ForEachTestGraphBuilder.get()
                .inbound(inbound)
                .forEach(forEachNode)
                .forEachSequence(toStringExecutionNode, eachNode2)
                .build();

        MessageAndContext event = newEventWithContent(
                ImmutableMap.of(
                        "key1", new NotSerializableObject(),
                        "key2", new NotSerializableObject()));
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .verifyErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                "ForEach Component Map value must be serializable.".equals(throwable.getMessage()));
    }

    static class NotSerializableObject {
        NotSerializableObject() {
        }
    }
}
