package com.reedelk.platform.component.router;


import com.reedelk.platform.commons.FindFirstSuccessorLeadingTo;
import com.reedelk.platform.flow.deserializer.AbstractDeserializer;
import com.reedelk.platform.flow.deserializer.DeserializerFactory;
import com.reedelk.platform.flow.deserializer.FlowDeserializerContext;
import com.reedelk.platform.graph.ExecutionGraph;
import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import com.reedelk.runtime.component.Stop;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.reedelk.runtime.commons.JsonParser.Implementor;
import static com.reedelk.runtime.commons.JsonParser.Router;

public class RouterDeserializer extends AbstractDeserializer {

    public RouterDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        super(graph, context);
    }

    @Override
    public ExecutionNode deserialize(ExecutionNode parent, JSONObject componentDefinition) {
        String componentName = Implementor.name(componentDefinition);

        ExecutionNode stopComponent = context.instantiateComponent(Stop.class);
        ExecutionNode routerExecutionNode = context.instantiateComponent(componentName);
        RouterWrapper routerWrapper = (RouterWrapper) routerExecutionNode.getComponent();

        graph.putEdge(parent, routerExecutionNode);

        JSONArray when = Router.when(componentDefinition);

        for (int i = 0; i < when.length(); i++) {
            ExecutionNode currentNode = routerExecutionNode;

            JSONObject component = when.getJSONObject(i);
            JSONArray next = Router.next(component);

            for (int j = 0; j < next.length(); j++) {
                JSONObject currentComponentDef = next.getJSONObject(j);
                ExecutionNode lastNode = DeserializerFactory.get()
                        .componentDefinition(currentComponentDef)
                        .parent(currentNode)
                        .context(context)
                        .graph(graph)
                        .deserialize();

                // The first component of A GIVEN router path,
                // must be added as a router expression pair.
                if (j == 0) {
                    DynamicString expression = context.converter().convert(DynamicString.class, component, Router.condition());
                    // 'lastNode' might be the last stop node from another scoped execution node (e.g. Fork, Router, Try-Catch).
                    // We must find the *FIRST* node leading to that stop node, otherwise we would not execute the nested
                    // scoped node components.
                    ExecutionNode firstRouterNode =
                            FindFirstSuccessorLeadingTo.of(graph, routerExecutionNode, lastNode);
                    routerWrapper.addExpressionAndPathPair(expression, firstRouterNode);
                }

                currentNode = lastNode;
            }

            graph.putEdge(currentNode, stopComponent);
        }

        // We add the stop execution node related to this router,
        // so that we can use it when building the flux definition
        // for this Router node. Otherwise we would have to find
        // the stop component by navigating the graph and finding the
        // the common stop node across all the nodes in the Router scope.
        routerWrapper.setEndOfRouterStopNode(stopComponent);

        return stopComponent;
    }

}
