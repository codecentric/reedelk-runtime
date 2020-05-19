package com.reedelk.platform.component.foreach;

import com.reedelk.platform.flow.deserializer.AbstractDeserializerTest;
import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.platform.test.utils.ComponentsBuilder;
import com.reedelk.runtime.component.ForEach;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class ForEachDeserializerTest extends AbstractDeserializerTest {

    private ExecutionNode forEachExecutionNode = new ExecutionNode(new ExecutionNode.ReferencePair<>(new ForEachWrapper()));

    private ForEachDeserializer deserializer;

    @BeforeEach
    public void setUp() {
        super.setUp();
        doReturn(forEachExecutionNode).when(context).instantiateComponent(ForEach.class.getName());
        deserializer = new ForEachDeserializer(graph, context);
    }

    @Test
    void shouldCorrectlyDeserializeForEachComponent() {
        // Given
        JSONArray nextArray =
                ComponentsBuilder.createNextComponentsArray(component6Name, component5Name);

        JSONObject componentDefinition = ComponentsBuilder.forComponent(ForEach.class)
                .with("next", nextArray)
                .build();

        // Mock the call made by FindFirstSuccessorLeadingTo.of function.
        doReturn(singletonList(component6))
                .when(graph)
                .successors(forEachExecutionNode);

        // When
        ExecutionNode lastNode = deserializer.deserialize(parent, componentDefinition);

        // Then

        // Assert Wrapper built correctly
        assertThat(lastNode).isEqualTo(stopNode1);

        ForEachWrapper forEachWrapper = (ForEachWrapper) forEachExecutionNode.getComponent();
        assertThat(forEachWrapper.getFirstEachNode()).isEqualTo(component6);
        assertThat(forEachWrapper.getStopNode()).isEqualTo(stopNode1);

        // Assert graph is built correctly
        verify(graph).putEdge(parent, forEachExecutionNode);
        verify(graph).putEdge(forEachExecutionNode, component6);
        verify(graph).putEdge(component6, component5);
        verify(graph).putEdge(component5, stopNode1);
    }
}
