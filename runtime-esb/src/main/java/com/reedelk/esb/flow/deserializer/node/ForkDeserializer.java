package com.reedelk.esb.flow.deserializer.node;

import com.reedelk.esb.component.ForkWrapper;
import com.reedelk.esb.execution.commons.FindFirstSuccessorLeadingTo;
import com.reedelk.esb.flow.deserializer.FlowDeserializerContext;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.component.Stop;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.reedelk.runtime.commons.JsonParser.Fork;
import static com.reedelk.runtime.commons.JsonParser.Implementor;

class ForkDeserializer extends AbstractDeserializer {

    ForkDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        super(graph, context);
    }

    @Override
    public ExecutionNode deserialize(ExecutionNode parent, JSONObject componentDefinition) {
        String componentName = Implementor.name(componentDefinition);

        ExecutionNode stopComponent = context.instantiateComponent(Stop.class);
        ExecutionNode forkExecutionNode = context.instantiateComponent(componentName);

        ForkWrapper forkWrapper = (ForkWrapper) forkExecutionNode.getComponent();

        graph.putEdge(parent, forkExecutionNode);

        JSONArray fork = Fork.fork(componentDefinition);
        for (int i = 0; i < fork.length(); i++) {

            JSONObject nextObject = fork.getJSONObject(i);
            JSONArray nextComponents = Fork.next(nextObject);

            ExecutionNode currentNode = forkExecutionNode;
            for (int j = 0; j < nextComponents.length(); j++) {

                JSONObject currentComponentDefinition = nextComponents.getJSONObject(j);
                ExecutionNode lastNode = ExecutionNodeDeserializer.get()
                        .componentDefinition(currentComponentDefinition)
                        .parent(currentNode)
                        .context(context)
                        .graph(graph)
                        .deserialize();

                // The first nextObject of A GIVEN fork path,
                // must be added as a fork execution node.
                if (j == 0) {
                    // 'lastNode' might be the last stop node from another scoped execution node (e.g. Fork, Router, Try-Catch).
                    // We must find the *FIRST* node leading to that stop node, otherwise we would not execute the nested
                    // scoped node components.
                    ExecutionNode firstForkNode =
                            FindFirstSuccessorLeadingTo.of(graph, forkExecutionNode, lastNode);
                    forkWrapper.addForkNode(firstForkNode);
                }

                currentNode = lastNode;
            }

            graph.putEdge(currentNode, stopComponent);
        }

        forkWrapper.setStopNode(stopComponent);

        return stopComponent;
    }
}
