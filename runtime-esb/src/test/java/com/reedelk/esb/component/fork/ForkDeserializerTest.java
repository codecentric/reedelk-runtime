package com.reedelk.esb.component.fork;

import com.reedelk.esb.flow.deserializer.AbstractDeserializerTest;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.esb.test.utils.ComponentsBuilder;
import com.reedelk.esb.test.utils.TestJoinComponent;
import com.reedelk.runtime.component.Fork;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

class ForkDeserializerTest extends AbstractDeserializerTest {

    private ExecutionNode forkExecutionNode = new ExecutionNode(new ExecutionNode.ReferencePair<>(new ForkWrapper()));

    private ForkDeserializer deserializer;

    @BeforeEach
    public void setUp() {
        super.setUp();
        doReturn(forkExecutionNode).when(context).instantiateComponent(Fork.class.getName());
        deserializer = new ForkDeserializer(graph, context);
    }

    @Test
    void shouldCorrectlyDeserializeForkJoinComponent() {
        // Given
        JSONArray forkArray = new JSONArray();
        forkArray.put(createNextObject(component6Name, component5Name));
        forkArray.put(createNextObject(component1Name, component4Name));

        JSONObject componentDefinition = ComponentsBuilder.forComponent(Fork.class)
                .with("fork", forkArray)
                .with("join", ComponentsBuilder.forComponent(TestJoinComponent.class)
                        .with("prop1", "Test")
                        .with("prop2", 3L)
                        .build())
                .build();

        // Mock the call made by FindFirstSuccessorLeadingTo.of function.
        doReturn(singletonList(component6), asList(component6, component1))
                .when(graph)
                .successors(forkExecutionNode);

        // When
        ExecutionNode lastNode = deserializer.deserialize(parent, componentDefinition);

        // Then
        assertThat(lastNode).isEqualTo(stopNode1);

        verify(graph).putEdge(parent, forkExecutionNode);

        // First Fork
        verify(graph).putEdge(forkExecutionNode, component6);
        verify(graph).putEdge(component6, component5);
        verify(graph).putEdge(component5, stopNode1);

        // Second Fork
        verify(graph).putEdge(forkExecutionNode, component1);
        verify(graph).putEdge(component1, component4);
        verify(graph).putEdge(component4, stopNode1);
    }

    private JSONObject createNextObject(String... componentNames) {
        JSONObject nextObject = new JSONObject();
        nextObject.put("next", ComponentsBuilder.createNextComponentsArray(componentNames));
        return nextObject;
    }
}
