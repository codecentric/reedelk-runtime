package com.reedelk.esb.component.trycatch;

import com.reedelk.esb.component.AbstractTestGraphBuilder;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.component.Stop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TryCatchTestGraphBuilder extends AbstractTestGraphBuilder {

    private Sequence trySequence;
    private Sequence catchSequence;

    private ExecutionNode inbound;
    private ExecutionNode tryCatchNode;

    private List<ExecutionNode> followingSequence = new ArrayList<>();

    public static TryCatchTestGraphBuilder get() {
        return new TryCatchTestGraphBuilder();
    }

    public TryCatchTestGraphBuilder inbound(ExecutionNode inbound) {
        this.inbound = inbound;
        return this;
    }

    public TryCatchTestGraphBuilder tryNodes(ExecutionNode ...trySequence) {
        this.trySequence = new Sequence(trySequence);
        return this;
    }

    public TryCatchTestGraphBuilder catchNodes(ExecutionNode ...catchSequence) {
        this.catchSequence = new Sequence(catchSequence);
        return this;
    }

    public TryCatchTestGraphBuilder tryCatchNode(ExecutionNode tryCatchNode) {
        this.tryCatchNode = tryCatchNode;
        return this;
    }

    public TryCatchTestGraphBuilder afterTryCatchSequence(ExecutionNode... following) {
        this.followingSequence = Arrays.asList(following);
        return this;
    }

    public ExecutionGraph build() {
        ExecutionNode endOfTryCatch = newExecutionNode(new Stop());
        TryCatchWrapper tryCatchWrapper = (TryCatchWrapper) tryCatchNode.getComponent();
        tryCatchWrapper.setStopNode(endOfTryCatch);

        ExecutionGraph graph = ExecutionGraph.build();
        graph.putEdge(null, inbound);
        graph.putEdge(inbound, tryCatchNode);

        buildSequence(graph, tryCatchNode, endOfTryCatch, trySequence.sequence);
        trySequence.sequence.stream().findFirst().ifPresent(tryCatchWrapper::setFirstTryNode);

        buildSequence(graph, tryCatchNode, endOfTryCatch, catchSequence.sequence);
        catchSequence.sequence.stream().findFirst().ifPresent(tryCatchWrapper::setFirstCatchNode);

        ExecutionNode endOfGraphNode = newExecutionNode(new Stop());
        if (followingSequence.size() > 0) {
            buildSequence(graph, endOfTryCatch, endOfGraphNode, followingSequence);
        } else {
            graph.putEdge(endOfTryCatch, endOfGraphNode);
        }

        return graph;
    }

    static class Sequence {
        List<ExecutionNode> sequence;
        Sequence(ExecutionNode[] sequence) {
            this.sequence = Arrays.asList(sequence);
        }
    }
}
