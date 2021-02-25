package de.codecentric.reedelk.platform.component.flowreference;

import de.codecentric.reedelk.platform.flow.deserializer.AbstractDeserializer;
import de.codecentric.reedelk.platform.flow.deserializer.DeserializerFactory;
import de.codecentric.reedelk.platform.flow.deserializer.FlowDeserializerContext;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.commons.Preconditions;
import de.codecentric.reedelk.runtime.commons.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;

import static de.codecentric.reedelk.runtime.api.commons.Preconditions.checkState;

public class FlowReferenceDeserializer extends AbstractDeserializer {

    public FlowReferenceDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        super(graph, context);
    }

    @Override
    public ExecutionNode deserialize(ExecutionNode parent, JSONObject componentDefinition) {
        String flowReference = JsonParser.FlowReference.ref(componentDefinition);

        Preconditions.checkState(flowReference != null,
                "ref property inside a FlowReference component cannot be null");

        Set<JSONObject> subflows = context.deserializedModule().getSubflows();

        JSONObject subflow = findSubflowByReference(subflows, flowReference);
        JSONArray subflowComponents = JsonParser.Subflow.subflow(subflow);

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
                .filter(subflow -> JsonParser.Subflow.id(subflow).equals(referenceName))
                .findFirst()
                .orElseThrow(() -> new PlatformException("Could not find Subflow with referenceId='" + referenceName + "'"));
    }
}
