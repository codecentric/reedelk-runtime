package com.reedelk.esb.component.foreach;

import com.reedelk.esb.flow.deserializer.FlowDeserializerContext;
import com.reedelk.esb.flow.deserializer.GenericComponentDeserializer;
import com.reedelk.esb.graph.ExecutionGraph;

public class ForEachDeserializer extends GenericComponentDeserializer {

    public ForEachDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        super(graph, context);
    }
}
