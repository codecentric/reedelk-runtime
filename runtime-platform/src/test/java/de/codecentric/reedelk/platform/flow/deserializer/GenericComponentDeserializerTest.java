package de.codecentric.reedelk.platform.flow.deserializer;

import de.codecentric.reedelk.platform.test.utils.ComponentsBuilder;
import de.codecentric.reedelk.platform.test.utils.TestComponent;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.api.component.Component;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Generic Component Builder")
class GenericComponentDeserializerTest {

    @Mock
    private ExecutionGraph graph;
    @Mock
    private ExecutionNode parent;
    @Mock
    private FlowDeserializerContext context;

    private GenericComponentDeserializer builder;

    @BeforeEach
    void setUp() {
        builder = new GenericComponentDeserializer(graph, context);
    }

    @Test
    void shouldCorrectlyPutEdgeBetweenParentAndDeSerializedExecutionNodeInGraph() {
        // Given
        JSONObject componentDefinition = ComponentsBuilder.forComponent(TestComponent.class).build();
        ExecutionNode executionNode = new ExecutionNode(new ExecutionNode.ReferencePair<>(new TestComponent()));
        mockInstantiateComponent(executionNode);

        // When
        builder.deserialize(parent, componentDefinition);

        // Then
        verify(graph).putEdge(parent, executionNode);
        verifyNoMoreInteractions(graph);
    }

    @Test
    void shouldCorrectlyInitializeInitializeExecutionNode() {
        // Given
        JSONObject componentDefinition = ComponentsBuilder.forComponent(TestComponent.class).build();
        ExecutionNode executionNode = mock(ExecutionNode.class);
        Component component = new TestComponent();
        doReturn(component).when(executionNode).getComponent();
        doReturn(executionNode).when(context).instantiateComponent(TestComponent.class.getName());

        // When
        builder.deserialize(parent, componentDefinition);

        // Then
        verify(executionNode).onInitializeEvent();
    }

    private void mockInstantiateComponent(ExecutionNode executionNode) {
        doReturn(executionNode)
                .when(context)
                .instantiateComponent(executionNode.getComponent().getClass().getName());
    }
}
