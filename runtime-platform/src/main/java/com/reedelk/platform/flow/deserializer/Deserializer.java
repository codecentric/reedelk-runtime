package com.reedelk.platform.flow.deserializer;

import com.reedelk.platform.graph.ExecutionNode;
import org.json.JSONObject;

public interface Deserializer {

    ExecutionNode deserialize(ExecutionNode parent, JSONObject componentDefinition);
}

