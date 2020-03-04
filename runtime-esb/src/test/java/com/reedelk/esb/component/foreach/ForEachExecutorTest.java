package com.reedelk.esb.component.foreach;

import com.reedelk.esb.execution.AbstractExecutionTest;
import com.reedelk.esb.execution.MessageAndContext;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

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

        // When
        MessageAndContext event = newEventWithContent(asList("one", "two"));
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains(asList("one-each1-each2", "two-each1-each2")))
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

        // When
        MessageAndContext event = newEventWithContent("one");
        Publisher<MessageAndContext> publisher = Mono.just(event);

        // When
        Publisher<MessageAndContext> endPublisher = executor.execute(publisher, forEachNode, graph);

        // Then
        StepVerifier.create(endPublisher)
                .assertNext(assertMessageContains(Collections.singletonList("one-each1-each2")))
                .verifyComplete();
    }
}
