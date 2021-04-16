package de.codecentric.reedelk.platform.component.trycatch;

import de.codecentric.reedelk.platform.commons.FindFirstSuccessorLeadingTo;
import de.codecentric.reedelk.platform.flow.deserializer.AbstractDeserializer;
import de.codecentric.reedelk.platform.flow.deserializer.DeserializerFactory;
import de.codecentric.reedelk.platform.flow.deserializer.FlowDeserializerContext;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.component.Stop;
import org.json.JSONArray;
import org.json.JSONObject;

import static de.codecentric.reedelk.runtime.commons.JsonParser.Implementor;
import static de.codecentric.reedelk.runtime.commons.JsonParser.TryCatch;

public class TryCatchDeserializer extends AbstractDeserializer {

    public TryCatchDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        super(graph, context);
    }

    @Override
    public ExecutionNode deserialize(ExecutionNode parent, JSONObject componentDefinition) {

        String componentName = Implementor.name(componentDefinition);

        ExecutionNode stopComponent = context.instantiateComponent(Stop.class);
        ExecutionNode tryCatchExecutionNode = context.instantiateComponent(componentName);

        TryCatchWrapper tryCatchWrapper = (TryCatchWrapper) tryCatchExecutionNode.getComponent();

        graph.putEdge(parent, tryCatchExecutionNode);

        ExecutionNode currentNode = tryCatchExecutionNode;

        JSONArray doTry = TryCatch.doTry(componentDefinition);

        for (int i = 0; i < doTry.length(); i++) {
            JSONObject currentComponentDefinition = doTry.getJSONObject(i);
            currentNode = DeserializerFactory.get()
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
            currentNode = DeserializerFactory.get()
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
