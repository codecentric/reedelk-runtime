package com.reedelk.esb.component.foreach;

import com.reedelk.esb.component.AbstractTestGraphBuilder;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.component.Stop;

import java.util.Arrays;
import java.util.List;

public class ForEachTestGraphBuilder extends AbstractTestGraphBuilder {

    private ExecutionNode forEach;
    private ExecutionNode inbound;
    private List<ExecutionNode> sequence;

    private ForEachTestGraphBuilder() {
    }

    public static ForEachTestGraphBuilder get() {
        return new ForEachTestGraphBuilder();
    }

    public ForEachTestGraphBuilder forEach(ExecutionNode forEach) {
        this.forEach = forEach;
        return this;
    }

    public ForEachTestGraphBuilder forEachSequence(ExecutionNode... sequence) {
        this.sequence = Arrays.asList(sequence);
        return this;
    }

    public ForEachTestGraphBuilder inbound(ExecutionNode inbound) {
        this.inbound = inbound;
        return this;
    }

    public ExecutionGraph build() {
        ExecutionGraph graph = ExecutionGraph.build();
        graph.putEdge(null, inbound);
        graph.putEdge(inbound, forEach);

        ExecutionNode endOfForEach = newExecutionNode(new Stop());
        ForEachWrapper forEachWrapper = (ForEachWrapper) forEach.getComponent();
        forEachWrapper.setStopNode(endOfForEach);

        buildSequence(graph, forEach, endOfForEach, sequence);
        if (sequence.size() > 0) {
            forEachWrapper.setFirstEachNode(sequence.get(0));
        }

        ExecutionNode endOfGraph = newExecutionNode(new Stop());
        graph.putEdge(endOfForEach, endOfGraph);

        return graph;
    }
}
