package com.reedelk.esb.component.foreach;

import com.reedelk.esb.component.AbstractTestGraphBuilder;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.component.Stop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForEachTestGraphBuilder extends AbstractTestGraphBuilder {

    private ExecutionNode join;
    private ExecutionNode forEach;
    private ExecutionNode inbound;
    private List<ExecutionNode> sequence = new ArrayList<>();
    private List<ExecutionNode> followingSequence = new ArrayList<>();

    private ForEachTestGraphBuilder() {
    }

    public static ForEachTestGraphBuilder get() {
        return new ForEachTestGraphBuilder();
    }

    public ForEachTestGraphBuilder join(ExecutionNode join) {
        this.join = join;
        return this;
    }

    public ForEachTestGraphBuilder inbound(ExecutionNode inbound) {
        this.inbound = inbound;
        return this;
    }

    public ForEachTestGraphBuilder forEach(ExecutionNode forEach) {
        this.forEach = forEach;
        return this;
    }

    public ForEachTestGraphBuilder forEachSequence(ExecutionNode... sequence) {
        this.sequence = Arrays.asList(sequence);
        return this;
    }

    public ForEachTestGraphBuilder afterForEachSequence(ExecutionNode... afterForkSequence) {
        this.followingSequence = Arrays.asList(afterForkSequence);
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

        ExecutionNode last = endOfForEach;

        if (join != null) {
            graph.putEdge(endOfForEach, join);
            last = join;
        }

        ExecutionNode endOfGraph = newExecutionNode(new Stop());
        if (followingSequence.size() > 0) {
            buildSequence(graph, last, endOfGraph, followingSequence);
        } else {
            graph.putEdge(last, endOfGraph);
        }

        return graph;
    }
}
