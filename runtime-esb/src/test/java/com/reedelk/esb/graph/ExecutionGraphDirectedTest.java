package com.reedelk.esb.graph;

import com.reedelk.esb.test.utils.TestComponent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExecutionGraphDirectedTest {

    @Test
    void shouldReturnTrueWhenEmpty() {
        // Given
        ExecutionGraphDirected graph = new ExecutionGraphDirected();

        // When
        boolean actual = graph.isEmpty();

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldReturnFalseWhenNotEmpty() {
        // Given
        ExecutionGraphDirected graph = new ExecutionGraphDirected();
        graph.addNode(new ExecutionNode(new ExecutionNode.ReferencePair<>(new TestComponent())));

        // When
        boolean actual = graph.isEmpty();

        // Then
        assertThat(actual).isFalse();
    }
}