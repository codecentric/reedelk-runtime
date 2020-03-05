package com.reedelk.esb.component.foreach;

import com.reedelk.esb.commons.FindFirstSuccessorLeadingTo;
import com.reedelk.esb.flow.deserializer.AbstractDeserializer;
import com.reedelk.esb.flow.deserializer.DeserializerFactory;
import com.reedelk.esb.flow.deserializer.FlowDeserializerContext;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.component.Stop;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.reedelk.runtime.commons.JsonParser.ForEach;
import static com.reedelk.runtime.commons.JsonParser.Implementor;

public class ForEachDeserializer extends AbstractDeserializer {

    public ForEachDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        super(graph, context);
    }

    @Override
    public ExecutionNode deserialize(ExecutionNode parent, JSONObject componentDefinition) {

        String componentName = Implementor.name(componentDefinition);

        ExecutionNode stopComponent = context.instantiateComponent(Stop.class);
        ExecutionNode forEachExecutionNode = context.instantiateComponent(componentName);

        ForEachWrapper forEachWrapper = (ForEachWrapper) forEachExecutionNode.getComponent();

        graph.putEdge(parent, forEachExecutionNode);

        ExecutionNode currentNode = forEachExecutionNode;

        JSONArray doEach = ForEach.next(componentDefinition);

        for (int i = 0; i < doEach.length(); i++) {
            JSONObject currentComponentDefinition = doEach.getJSONObject(i);
            currentNode = DeserializerFactory.get()
                    .componentDefinition(currentComponentDefinition)
                    .parent(currentNode)
                    .context(context)
                    .graph(graph)
                    .deserialize();

            // The first each node must be set to the wrapper object.
            // This is required for the executor to know which node
            // to follow in the each block.
            if (i == 0) {
                // 'currentNode' might be the last stop node from another scoped execution node (e.g Fork, Router, Try-Catch, ForEach).
                // We must find the *FIRST* node leading to that stop node, otherwise we would not execute the nested
                // scoped node's components.
                ExecutionNode firstEachNode =
                        FindFirstSuccessorLeadingTo.of(graph, forEachExecutionNode, currentNode);
                forEachWrapper.setFirstEachNode(firstEachNode);

            }
        }

        graph.putEdge(currentNode, stopComponent);

        forEachWrapper.setStopNode(stopComponent);

        return stopComponent;
    }
}
