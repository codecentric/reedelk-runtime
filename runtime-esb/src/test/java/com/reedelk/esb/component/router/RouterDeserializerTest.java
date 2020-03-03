package com.reedelk.esb.component.router;

import com.reedelk.esb.flow.deserializer.AbstractDeserializerTest;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.esb.graph.ExecutionNode.ReferencePair;
import com.reedelk.esb.test.utils.ComponentsBuilder;
import com.reedelk.runtime.component.Router;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.reedelk.runtime.component.Router.DEFAULT_CONDITION;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RouterDeserializerTest extends AbstractDeserializerTest {

    private ExecutionNode routerExecutionNode = new ExecutionNode(new ReferencePair<>(new RouterWrapper()));

    private RouterDeserializer deserializer;

    @BeforeEach
    public void setUp() {
        super.setUp();
        doReturn(routerExecutionNode).when(context).instantiateComponent(Router.class.getName());
        deserializer = new RouterDeserializer(graph, context);
    }

    @Test
    void shouldCorrectlyDeserializeRouterComponent() {
        // Given
        JSONArray whenArray = new JSONArray();
        whenArray.put(conditionalBranch("#[1 == 1]", component3Name, component1Name));
        whenArray.put(conditionalBranch("#['hello' == 'hello1']", component2Name, component4Name));
        whenArray.put(conditionalBranch(DEFAULT_CONDITION.value(), component6Name, component5Name));

        JSONObject componentDefinition = ComponentsBuilder.forComponent(Router.class)
                .with("when", whenArray)
                .build();

        // Mock the call made by FindFirstSuccessorLeadingTo.of function.
        doReturn(singletonList(component3),
                asList(component3, component2),
                asList(component3, component2, component6))
                .when(graph)
                .successors(routerExecutionNode);

        // When
        ExecutionNode lastNode = deserializer.deserialize(parent, componentDefinition);

        // Then
        assertThat(lastNode).isEqualTo(stopNode1);

        verify(graph).putEdge(parent, routerExecutionNode);

        // First condition
        verify(graph).putEdge(routerExecutionNode, component3);
        verify(graph).putEdge(component3, component1);
        verify(graph).putEdge(component1, stopNode1);

        // Second condition
        verify(graph).putEdge(routerExecutionNode, component2);
        verify(graph).putEdge(component2, component4);
        verify(graph).putEdge(component4, stopNode1);

        // Otherwise
        verify(graph).putEdge(routerExecutionNode, component6);
        verify(graph).putEdge(component6, component5);
        verify(graph).putEdge(component5, stopNode1);

        verifyNoMoreInteractions(parent);
    }

    private JSONObject conditionalBranch(String condition, String... componentsNames) {
        JSONObject object = new JSONObject();
        object.put("condition", condition);
        object.put("next", ComponentsBuilder.createNextComponentsArray(componentsNames));
        return object;
    }
}
