package com.reedelk.esb.component.trycatch;

import com.reedelk.esb.flow.deserializer.AbstractDeserializerTest;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.esb.test.utils.ComponentsBuilder;
import com.reedelk.runtime.component.TryCatch;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TryCatchDeserializerTest extends AbstractDeserializerTest {

    private ExecutionNode tryCatchExecutionNode1 = new ExecutionNode(new ExecutionNode.ReferencePair<>(new TryCatchWrapper()));
    private ExecutionNode tryCatchExecutionNode2 = new ExecutionNode(new ExecutionNode.ReferencePair<>(new TryCatchWrapper()));

    private TryCatchDeserializer deserializer;

    @BeforeEach
    public void setUp() {
        super.setUp();
        doReturn(tryCatchExecutionNode1, tryCatchExecutionNode2)
                .when(context)
                .instantiateComponent(TryCatch.class.getName());
        deserializer = new TryCatchDeserializer(graph, context);
    }

    @Test
    void shouldCorrectlyDeserializeTryCatchComponent() {
        // Given
        JSONArray tryArray = ComponentsBuilder.createNextComponentsArray(component1Name, component2Name);
        JSONArray catchArray = ComponentsBuilder.createNextComponentsArray(component3Name, component4Name);
        JSONObject componentDefinition = ComponentsBuilder.forComponent(TryCatch.class)
                .with("try", tryArray)
                .with("catch", catchArray)
                .build();

        // Mock the call made by FindFirstSuccessorLeadingTo.of function.
        doReturn(singletonList(component1), asList(component1, component3))
                .when(graph)
                .successors(tryCatchExecutionNode1);

        // When
        ExecutionNode lastNode = deserializer.deserialize(parent, componentDefinition);

        // Then
        assertThat(lastNode).isEqualTo(stopNode1);

        verify(graph).putEdge(parent, tryCatchExecutionNode1);
        verify(graph).putEdge(tryCatchExecutionNode1, component1);
        verify(graph).putEdge(tryCatchExecutionNode1, component3);

        verify(graph).putEdge(component1, component2);
        verify(graph).putEdge(component3, component4);
        verify(graph).putEdge(component2, stopNode1);
        verify(graph).putEdge(component4, stopNode1);
    }

    @Test
    void shouldCorrectlyDeserializeNestedTryCatchComponent() {
        // Given
        JSONArray nestedTryArray = ComponentsBuilder.createNextComponentsArray(component5Name, component6Name);
        JSONArray nestedCatchArray = ComponentsBuilder.createNextComponentsArray(component7Name, component8Name);
        JSONObject nestedTryCatchDefinition = ComponentsBuilder.forComponent(TryCatch.class)
                .with("try", nestedTryArray)
                .with("catch", nestedCatchArray)
                .build();

        // Mock the call made by FindFirstSuccessorLeadingTo.of function.
        doReturn(singletonList(component5), asList(component5, component7)).when(graph).successors(tryCatchExecutionNode2);
        doReturn(singletonList(component6)).when(graph).successors(component5);
        doReturn(singletonList(stopNode2)).when(graph).successors(component6);
        doReturn(singletonList(component8)).when(graph).successors(component7);
        doReturn(emptyList()).when(graph).successors(stopNode2);


        JSONArray tryArray = ComponentsBuilder.createNextComponentsArray(component1Name, component2Name);
        JSONArray catchArray = new JSONArray();
        catchArray.put(nestedTryCatchDefinition);
        catchArray.put(ComponentsBuilder.forComponent(component4Name)
                .with("stringProperty", "Test")
                .with("longProperty", 3L)
                .build());

        JSONObject tryCatchDefinition = ComponentsBuilder.forComponent(TryCatch.class)
                .with("try", tryArray)
                .with("catch", catchArray)
                .build();

        // Mock the calls made by FindFirstSuccessorLeadingTo function.
        doReturn(singletonList(component1), asList(component1, tryCatchExecutionNode2)).when(graph).successors(tryCatchExecutionNode1);
        doReturn(singletonList(component2)).when(graph).successors(component1);
        doReturn(singletonList(stopNode1)).when(graph).successors(component2);
        doReturn(emptyList()).when(graph).successors(stopNode1);

        // When
        ExecutionNode lastNode = deserializer.deserialize(parent, tryCatchDefinition);

        // Then
        assertThat(lastNode).isEqualTo(stopNode1);

        verify(graph).putEdge(parent, tryCatchExecutionNode1);
        verify(graph).putEdge(tryCatchExecutionNode1, component1);
        verify(graph).putEdge(tryCatchExecutionNode1, tryCatchExecutionNode2);
        verify(graph).putEdge(component1, component2);
        verify(graph).putEdge(component2, stopNode1);
        verify(graph).putEdge(tryCatchExecutionNode2, component5);
        verify(graph).putEdge(component5, component6);
        verify(graph).putEdge(component6, stopNode2);
        verify(graph).putEdge(tryCatchExecutionNode2, component7);
        verify(graph).putEdge(component7, component8);
        verify(graph).putEdge(component8, stopNode2);
        verify(graph).putEdge(stopNode2, component4);
        verify(graph).putEdge(component4, stopNode1);

        verifyNoMoreInteractions(graph);
    }
}
