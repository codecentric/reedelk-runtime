package com.reedelk.platform.graph;

import com.reedelk.platform.graph.ExecutionNode.ReferencePair;
import com.reedelk.platform.test.utils.AnotherTestComponent;
import com.reedelk.platform.test.utils.TestComponent;
import com.reedelk.platform.test.utils.TestInboundComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.introspection.FieldSupport.EXTRACTION;

@ExtendWith(MockitoExtension.class)
class ExecutionGraphTest {

    private ExecutionGraph graph;

    @BeforeEach
    void setUp() {
        graph = ExecutionGraph.build();
    }

    @Test
    void shouldThrowExceptionWhenPutEdgeAndN1NotAddedAlready() {
        // Given
        ExecutionNode n1 = new ExecutionNode(new ReferencePair<>(new TestInboundComponent()));
        ExecutionNode n2 = new ExecutionNode(new ReferencePair<>(new TestComponent()));

        // When
        Assertions.assertThrows(IllegalStateException.class, () -> graph.putEdge(n1, n2));
    }

    @Test
    void shouldSetRootElementWhenN1IsNull() {
        // Given
        ExecutionNode n1 = new ExecutionNode(new ReferencePair<>(new TestInboundComponent()));

        // When
        graph.putEdge(null, n1);

        // Then
        ExecutionNode root = EXTRACTION.fieldValue("root", ExecutionNode.class, graph);
        assertThat(root).isEqualTo(n1);
    }

    @Test
    void shouldThrowExceptionWhenRootAlreadyPresentAndNXIsNull() {
        // Given
        ExecutionNode n1 = new ExecutionNode(new ReferencePair<>(new TestInboundComponent()));
        ExecutionNode n2 = new ExecutionNode(new ReferencePair<>(new TestInboundComponent()));
        graph.putEdge(null, n1);

        // Expect
        Assertions.assertThrows(IllegalStateException.class, () -> graph.putEdge(null, n2));
    }

    @Test
    void shouldThrowExceptionWhenRootIsNotInbound() {
        // Given
        ExecutionNode n1 = new ExecutionNode(new ReferencePair<>(new TestComponent()));

        // Expect
        Assertions.assertThrows(IllegalStateException.class, () -> graph.putEdge(null, n1));
    }

    @Test
    void shouldThrowExceptionWhenAddingANodeWithoutN1AddedBefore() {
        // Given
        ExecutionNode n1 = new ExecutionNode(new ReferencePair<>(new TestInboundComponent()));
        ExecutionNode n2 = new ExecutionNode(new ReferencePair<>(new TestComponent()));

        // When
        Assertions.assertThrows(IllegalStateException.class, () -> graph.putEdge(n1, n2));
    }

    @Test
    void shouldSuccessorsReturnCorrectSuccessors() {
        // Given
        ExecutionNode n1 = new ExecutionNode(new ReferencePair<>(new TestInboundComponent()));
        ExecutionNode n2 = new ExecutionNode(new ReferencePair<>(new TestComponent()));
        ExecutionNode n3 = new ExecutionNode(new ReferencePair<>(new TestComponent()));
        ExecutionNode n4 = new ExecutionNode(new ReferencePair<>(new TestComponent()));

        graph.putEdge(null, n1);
        graph.putEdge(n1, n2);
        graph.putEdge(n1, n3);
        graph.putEdge(n3, n4);

        // When
        Collection<ExecutionNode> successors = graph.successors(n1);

        // Then
        assertThat(successors).containsExactlyInAnyOrder(n2, n3);
    }

    @Test
    void shouldApplyOnNodesTraverseAllNodes() {
        // Given
        ExecutionNode n1 = new ExecutionNode(new ReferencePair<>(new TestInboundComponent()));
        ExecutionNode n2 = new ExecutionNode(new ReferencePair<>(new TestComponent()));
        ExecutionNode n3 = new ExecutionNode(new ReferencePair<>(new TestComponent()));
        ExecutionNode n4 = new ExecutionNode(new ReferencePair<>(new TestComponent()));

        graph.putEdge(null, n1);
        graph.putEdge(n1, n2);
        graph.putEdge(n1, n3);
        graph.putEdge(n3, n4);

        Collection<ExecutionNode> visited = new ArrayList<>();

        // When
        graph.applyOnNodes(visited::add);

        // Then
        assertThat(visited).containsExactlyInAnyOrder(n1, n2, n3, n4);
    }

    @Test
    void shouldFindOneReturnEmpty() {
        // Given
        ExecutionNode n1 = new ExecutionNode(new ReferencePair<>(new TestInboundComponent()));
        ExecutionNode n2 = new ExecutionNode(new ReferencePair<>(new TestComponent()));

        graph.putEdge(null, n1);
        graph.putEdge(n1, n2);

        // When
        Optional<ExecutionNode> maybeOne = graph.findOne(executionNode ->
                executionNode.getComponent().getClass().equals(AnotherTestComponent.class));

        // Then
        assertThat(maybeOne).isEmpty();
    }

    @Test
    void shouldFindOneReturnExistingNodeMatchingCondition() {
        // Given
        ExecutionNode n1 = new ExecutionNode(new ReferencePair<>(new TestInboundComponent()));
        ExecutionNode n2 = new ExecutionNode(new ReferencePair<>(new TestComponent()));

        graph.putEdge(null, n1);
        graph.putEdge(n1, n2);

        // When
        Optional<ExecutionNode> maybeOne = graph.findOne(executionNode ->
                executionNode.getComponent().getClass().equals(TestComponent.class));

        // Then
        assertThat(maybeOne).get().isEqualTo(n2);
    }

    @Test
    void shouldReturnEmptyTrue() {
        // When
        boolean actual = graph.isEmpty();

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldReturnEmptyFalse() {
        // Given
        ExecutionNode n1 = new ExecutionNode(new ReferencePair<>(new TestInboundComponent()));
        graph.putEdge(null, n1);

        // When
        boolean actual = graph.isEmpty();

        // Then
        assertThat(actual).isFalse();
    }
}
