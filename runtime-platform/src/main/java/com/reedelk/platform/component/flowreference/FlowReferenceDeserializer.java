package com.reedelk.platform.component.flowreference;

import com.reedelk.platform.flow.deserializer.AbstractDeserializer;
import com.reedelk.platform.flow.deserializer.DeserializerFactory;
import com.reedelk.platform.flow.deserializer.FlowDeserializerContext;
import com.reedelk.platform.graph.ExecutionGraph;
import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.runtime.api.exception.PlatformException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;

import static com.reedelk.runtime.api.commons.Preconditions.checkState;
import static com.reedelk.runtime.commons.JsonParser.FlowReference;
import static com.reedelk.runtime.commons.JsonParser.Subflow;

public class FlowReferenceDeserializer extends AbstractDeserializer {

    public FlowReferenceDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        super(graph, context);
    }

    @Override
    public ExecutionNode deserialize(ExecutionNode parent, JSONObject componentDefinition) {
        String flowReference = FlowReference.ref(componentDefinition);

        checkState(flowReference != null,
                "ref property inside a FlowReference component cannot be null");

        Set<JSONObject> subflows = context.deserializedModule().getSubflows();

        JSONObject subflow = findSubflowByReference(subflows, flowReference);
        JSONArray subflowComponents = Subflow.subflow(subflow);

        ExecutionNode currentNode = parent;
        for (int i = 0; i < subflowComponents.length(); i++) {
            JSONObject currentComponent = subflowComponents.getJSONObject(i);

            currentNode = DeserializerFactory.get()
                    .componentDefinition(currentComponent)
                    .parent(currentNode)
                    .context(context)
                    .graph(graph)
                    .deserialize();
        }

        return currentNode;
    }

    private JSONObject findSubflowByReference(Set<JSONObject> subflows, String referenceName) {
        return subflows.stream()
                .filter(subflow -> Subflow.id(subflow).equals(referenceName))
                .findFirst()
                .orElseThrow(() -> new PlatformException("Could not find Subflow with referenceId='" + referenceName + "'"));
    }
}
