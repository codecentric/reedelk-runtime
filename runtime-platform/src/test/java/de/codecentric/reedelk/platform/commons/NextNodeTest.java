package de.codecentric.reedelk.platform.commons;

import de.codecentric.reedelk.platform.execution.AbstractExecutionTest;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.platform.test.utils.TestComponent;
import de.codecentric.reedelk.platform.test.utils.TestInboundComponent;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class NextNodeTest {

    private ExecutionNode inbound = AbstractExecutionTest.newExecutionNode(new TestInboundComponent());
    private ExecutionNode component1 = AbstractExecutionTest.newExecutionNode(new TestComponent());
    private ExecutionNode component2 = AbstractExecutionTest.newExecutionNode(new TestComponent());

    @Test
    void shouldReturnEmptyWhenNextNodeOfNull() {
        // Given
        ExecutionGraph graph = ExecutionGraph.build();
        graph.putEdge(null, inbound);
        graph.putEdge(inbound, component1);
        graph.putEdge(component1, component2);

        // When
        Optional<ExecutionNode> actual = NextNode.of(null, graph);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnCorrectNextNode() {
        // Given
        ExecutionGraph graph = ExecutionGraph.build();
        graph.putEdge(null, inbound);
        graph.putEdge(inbound, component1);
        graph.putEdge(component1, component2);

        // When
        Optional<ExecutionNode> actual = NextNode.of(component1, graph);

        // Then
        assertThat(actual).hasValue(component2);
    }

    @Test
    void shouldReturnEmptyWhenNextNodeOfLastNodeOfGraph() {
        // Given
        ExecutionGraph graph = ExecutionGraph.build();
        graph.putEdge(null, inbound);
        graph.putEdge(inbound, component1);
        graph.putEdge(component1, component2);

        // When
        Optional<ExecutionNode> actual = NextNode.of(component2, graph);

        // Then
        assertThat(actual).isEmpty();
    }
}
