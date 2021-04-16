package de.codecentric.reedelk.platform.flow.deserializer;


import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.commons.JsonParser;
import de.codecentric.reedelk.runtime.component.Stop;
import org.json.JSONArray;
import org.json.JSONObject;

import static de.codecentric.reedelk.runtime.api.commons.Preconditions.checkState;

public class FlowDeserializer {

    private final FlowDeserializerContext context;

    public FlowDeserializer(FlowDeserializerContext context) {
        this.context = context;
    }

    public void deserialize(ExecutionGraph flowGraph, JSONObject flowStructure) {
        JSONArray flowComponents = JsonParser.Flow.flow(flowStructure);

        ExecutionNode current = null;
        for (Object componentDefinitionObject : flowComponents) {
            checkState(componentDefinitionObject instanceof JSONObject, "not a JSON Object");

            JSONObject componentDefinition = (JSONObject) componentDefinitionObject;

            current = DeserializerFactory.get()
                    .componentDefinition(componentDefinition)
                    .context(context)
                    .graph(flowGraph)
                    .parent(current)
                    .deserialize();
        }

        // If the Flow does not contain any component,
        // we don't add the stop node because it only make
        // sense when there is at least one component.
        if (!flowComponents.isEmpty()) {
            // Last node of the graph is always a Stop node.
            ExecutionNode stopNode = context.instantiateComponent(Stop.class);
            flowGraph.putEdge(current, stopNode);
        }
    }
}
