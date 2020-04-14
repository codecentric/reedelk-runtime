package com.reedelk.platform.flow.deserializer;

import com.reedelk.platform.graph.ExecutionGraph;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FlowDeserializerTest {

    @Mock
    private FlowDeserializerContext context;

    private FlowDeserializer deserializer;

    @BeforeEach
    void setUp() {
        deserializer = new FlowDeserializer(context);
    }

    @Test
    void shouldNotAddAnyNodeToTheGraphWhenFlowDoesNotContainAnyComponent() {
        // Given
        JSONObject flowStructure = new JSONObject();
        // Simulate an empty Flow: i.e a Flow without any component in it.
        flowStructure.put("flow", new JSONArray());

        ExecutionGraph graph =  ExecutionGraph.build();

        // When
        deserializer.deserialize(graph, flowStructure);

        // Then
        assertThat(graph.isEmpty()).isTrue();
    }
}
