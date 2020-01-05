package com.reedelk.esb.flow.deserializer.node;

import com.reedelk.esb.component.TryCatchWrapper;
import com.reedelk.esb.execution.commons.FindFirstSuccessorLeadingTo;
import com.reedelk.esb.flow.deserializer.FlowDeserializerContext;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.component.Stop;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.reedelk.runtime.commons.JsonParser.Implementor;
import static com.reedelk.runtime.commons.JsonParser.TryCatch;

public class TryCatchDeserializer extends AbstractDeserializer {

    TryCatchDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        super(graph, context);
    }

    @Override
    public ExecutionNode deserialize(ExecutionNode parent, JSONObject componentDefinition) {

        String componentName = Implementor.name(componentDefinition);

        ExecutionNode tryCatchExecutionNode = context.instantiateComponent(componentName);

        ExecutionNode stopComponent = context.instantiateComponent(Stop.class);

        TryCatchWrapper tryCatchWrapper = (TryCatchWrapper) tryCatchExecutionNode.getComponent();

        graph.putEdge(parent, tryCatchExecutionNode);

        ExecutionNode currentNode = tryCatchExecutionNode;

        JSONArray doTry = TryCatch.doTry(componentDefinition);

        for (int i = 0; i < doTry.length(); i++) {
            JSONObject currentComponentDefinition = doTry.getJSONObject(i);
            currentNode = ExecutionNodeDeserializer.get()
                    .componentDefinition(currentComponentDefinition)
                    .parent(currentNode)
                    .context(context)
                    .graph(graph)
                    .deserialize();

            // The first try node must be set to the wrapper object.
            // This is required for the executor to know which node
            // to follow in the try block.
            if (i == 0) {
                // 'currentNode' might be the last stop node from another scoped execution node (e.g. Fork, Router, Try-Catch).
                // We must find the *FIRST* node leading to that stop node, otherwise we would not execute the nested
                // scoped node's components.
                ExecutionNode firstTryNode =
                        FindFirstSuccessorLeadingTo.of(graph, tryCatchExecutionNode, currentNode);
                tryCatchWrapper.setFirstTryNode(firstTryNode);
            }
        }

        graph.putEdge(currentNode, stopComponent);

        currentNode = tryCatchExecutionNode;

        JSONArray doCatch = TryCatch.doCatch(componentDefinition);

        for (int i = 0; i < doCatch.length(); i++) {
            JSONObject currentComponentDefinition = doCatch.getJSONObject(i);
            currentNode = ExecutionNodeDeserializer.get()
                    .componentDefinition(currentComponentDefinition)
                    .parent(currentNode)
                    .context(context)
                    .graph(graph)
                    .deserialize();

            // The first catch node must be set to the wrapper object.
            // This is required for the executor to know which node
            // to follow in the catch block when an exception has
            // occurred in the try flow.
            if (i == 0) {
                // 'currentNode' might be the last stop node from another scoped execution node (e.g. Fork, Router, Try-Catch).
                // We must find the *FIRST* node leading to that stop node, otherwise we would not execute the nested
                // scoped node's components.
                ExecutionNode firstCatchNode =
                        FindFirstSuccessorLeadingTo.of(graph, tryCatchExecutionNode, currentNode);
                tryCatchWrapper.setFirstCatchNode(firstCatchNode);
            }
        }

        graph.putEdge(currentNode, stopComponent);

        tryCatchWrapper.setStopNode(stopComponent);

        return stopComponent;
    }
}
