package com.reedelk.platform.component.flowreference;

import com.reedelk.platform.flow.deserializer.AbstractDeserializerTest;
import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.platform.module.DeSerializedModule;
import com.reedelk.platform.test.utils.ComponentsBuilder;
import com.reedelk.runtime.component.FlowReference;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FlowReferenceDeserializerTest extends AbstractDeserializerTest {

    @Test
    void shouldCorrectlyHandleFlowReferenceComponent() {
        // Given
        JSONObject mySubflowDefinition = ComponentsBuilder.create()
                .with("id", "subflow1")
                .with("subflow", ComponentsBuilder.createNextComponentsArray(component3Name, component4Name, component2Name))
                .build();

        Set<JSONObject> subflows = new HashSet<>();
        subflows.add(mySubflowDefinition);

        DeSerializedModule deSerializedModule = new DeSerializedModule(emptySet(), subflows, emptySet(), emptySet(), emptySet());

        doReturn(deSerializedModule).when(context).deserializedModule();

        JSONObject componentDefinition = ComponentsBuilder.forComponent(FlowReference.class)
                .with("ref", "subflow1")
                .build();

        FlowReferenceDeserializer builder = new FlowReferenceDeserializer(graph, context);

        // When
        ExecutionNode lastNode = builder.deserialize(component1, componentDefinition);

        // Then
        assertThat(lastNode).isEqualTo(component2);

        verify(graph).putEdge(component1, component3);
        verify(graph).putEdge(component3, component4);
        verify(graph).putEdge(component4, component2);
        verifyNoMoreInteractions(graph);
    }
}
