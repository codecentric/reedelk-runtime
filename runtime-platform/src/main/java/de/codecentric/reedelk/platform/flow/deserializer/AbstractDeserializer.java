package de.codecentric.reedelk.platform.flow.deserializer;

import de.codecentric.reedelk.platform.graph.ExecutionGraph;

public abstract class AbstractDeserializer implements Deserializer {

    protected final ExecutionGraph graph;
    protected final FlowDeserializerContext context;

    public AbstractDeserializer(ExecutionGraph graph, FlowDeserializerContext context) {
        this.graph = graph;
        this.context = context;
    }
}
