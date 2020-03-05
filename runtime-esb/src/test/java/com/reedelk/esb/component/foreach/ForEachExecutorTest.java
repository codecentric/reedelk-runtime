package com.reedelk.esb.component.foreach;

import com.reedelk.esb.execution.AbstractExecutionTest;
import com.reedelk.esb.execution.MessageAndContext;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static java.util.Arrays.asList;
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

        MessageAndContext event = newEventWithContent(Arrays.asList("one","two"));
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

        MessageAndContext event = newEventWithContent(Arrays.asList("one","two"));
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
}
