package com.reedelk.esb.flow.deserializer.node;

import com.reedelk.esb.flow.deserializer.FlowDeserializerContext;
import com.reedelk.esb.graph.ExecutionGraph;

public abstract class AbstractDeserializer implements Deserializer {

    protected final ExecutionGraph graph;
    protected final FlowDeserializerContext context;

    AbstractDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        this.graph = graph;
        this.context = context;
    }
}
