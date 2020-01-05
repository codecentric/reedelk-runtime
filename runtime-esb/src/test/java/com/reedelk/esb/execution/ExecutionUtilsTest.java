package com.reedelk.esb.execution;

import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.esb.test.utils.TestComponent;
import com.reedelk.esb.test.utils.TestInboundComponent;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.reedelk.esb.execution.AbstractExecutionTest.newExecutionNode;
import static org.assertj.core.api.Assertions.assertThat;

class ExecutionUtilsTest {

    private ExecutionNode inbound = newExecutionNode(new TestInboundComponent());
    private ExecutionNode component1 = newExecutionNode(new TestComponent());
    private ExecutionNode component2 = newExecutionNode(new TestComponent());

    @Test
    void shouldReturnEmptyWhenNextNodeOfNull() {
        // Given
        ExecutionGraph graph = ExecutionGraph.build();
        graph.putEdge(null, inbound);
        graph.putEdge(inbound, component1);
        graph.putEdge(component1, component2);

        // When
        Optional<ExecutionNode> actual = ExecutionUtils.nextNodeOf(null, graph);

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
        Optional<ExecutionNode> actual = ExecutionUtils.nextNodeOf(component1, graph);

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
        Optional<ExecutionNode> actual = ExecutionUtils.nextNodeOf(component2, graph);

        // Then
        assertThat(actual).isEmpty();
    }
}