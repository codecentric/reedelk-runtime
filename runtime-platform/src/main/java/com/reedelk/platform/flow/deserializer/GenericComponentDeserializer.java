package com.reedelk.platform.flow.deserializer;

import com.reedelk.platform.graph.ExecutionGraph;
import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.commons.JsonParser;
import org.json.JSONObject;

public class GenericComponentDeserializer extends AbstractDeserializer {

    protected GenericComponentDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        super(graph, context);
    }

    @Override
    public ExecutionNode deserialize(ExecutionNode parent, JSONObject componentDefinition) {
        String componentName = JsonParser.Implementor.name(componentDefinition);

        ExecutionNode executionNode = context.instantiateComponent(componentName);
        Component component = executionNode.getComponent();

        GenericComponentDefinitionDeserializer deserializer = new GenericComponentDefinitionDeserializer(executionNode, context);
        deserializer.deserialize(componentDefinition, component);

        // We must call on initialize so that all the component's and dependencies
        // initialize() method are being called for this component.
        executionNode.onInitializeEvent();

        graph.putEdge(parent, executionNode);
        return executionNode;
    }
}
