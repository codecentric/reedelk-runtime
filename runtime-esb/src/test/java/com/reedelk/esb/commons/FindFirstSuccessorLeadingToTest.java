package com.reedelk.esb.commons;

import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.esb.test.utils.TestComponent;
import com.reedelk.esb.test.utils.TestInboundComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FindFirstSuccessorLeadingToTest {

    private ExecutionGraph graph;

    private ExecutionNode root = new ExecutionNode(new ExecutionNode.ReferencePair<>(new TestInboundComponent()));
    private ExecutionNode component1 = new ExecutionNode(new ExecutionNode.ReferencePair<>(new TestComponent()));
    private ExecutionNode component2 = new ExecutionNode(new ExecutionNode.ReferencePair<>(new TestComponent()));
    private ExecutionNode component3 = new ExecutionNode(new ExecutionNode.ReferencePair<>(new TestComponent()));
    private ExecutionNode component4 = new ExecutionNode(new ExecutionNode.ReferencePair<>(new TestComponent()));

    @BeforeEach
    void setUp() {
        graph = ExecutionGraph.build();
    }

    @Test
    void shouldReturnFirstSuccessorLeadingToExecutionNode() {
        // Given
        graph.putEdge(null, root);
        graph.putEdge(root, component1);
        graph.putEdge(component1, component2);
        graph.putEdge(component1, component3);
        graph.putEdge(component2, component4);

        // When
        ExecutionNode successor = FindFirstSuccessorLeadingTo.of(graph, component1, component4);

        // Then
        assertThat(successor).isEqualTo(component2);
    }

    @Test
    void shouldReturnFirstSuccessorLeadingToExecutionNodeWhenExecutionNodeIsFirstChild() {
        // Given
        graph.putEdge(null, root);
        graph.putEdge(root, component1);
        graph.putEdge(component1, component2);
        graph.putEdge(component1, component3);

        // When
        ExecutionNode successor = FindFirstSuccessorLeadingTo.of(graph, component1, component2);

        // Then
        assertThat(successor).isEqualTo(component2);
    }

    @Test
    void shouldReturnFirstSuccessorLeadingToExecutionNodeThrowExceptionWhenExecutionNodeNotFound() {
        // Given
        graph.putEdge(null, root);
        graph.putEdge(root, component1);
        graph.putEdge(component1, component2);
        graph.putEdge(component1, component3);

        // When
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> FindFirstSuccessorLeadingTo.of(graph, component1, component4));

        // Then
        assertThat(thrown).isNotNull();
        assertThat(thrown).hasMessage("Could not find first successor of component=[com.reedelk.esb.test.utils.TestComponent], leading to component=[com.reedelk.esb.test.utils.TestComponent]");
    }
}
