package de.codecentric.reedelk.platform.flow.deserializer;

import de.codecentric.reedelk.platform.graph.ExecutionNode;
import org.json.JSONObject;

public interface Deserializer {

    ExecutionNode deserialize(ExecutionNode parent, JSONObject componentDefinition);
}

