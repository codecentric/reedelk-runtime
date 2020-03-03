package com.reedelk.esb.flow.deserializer;

import com.reedelk.esb.graph.ExecutionGraph;

public abstract class AbstractDeserializer implements Deserializer {

    protected final ExecutionGraph graph;
    protected final FlowDeserializerContext context;

    public AbstractDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        this.graph = graph;
        this.context = context;
    }
}
