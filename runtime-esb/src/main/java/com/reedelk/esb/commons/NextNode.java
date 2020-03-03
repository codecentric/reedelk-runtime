package com.reedelk.esb.commons;

import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;

import java.util.Collection;
import java.util.Optional;

import static java.lang.String.format;

public class NextNode {

    private NextNode() {
    }

    public static Optional<ExecutionNode> of(ExecutionNode current, ExecutionGraph graph) {
        return current == null ?
                Optional.empty() :
                graph.successors(current).stream().findFirst();
    }
    
    /**
     * Returns the successor node of the current node and it throws
     * an exception if a node was not found.
     *
     * @param current the current node for which we want to get the successor.
     * @param graph   the execution graph the current node belongs to.
     * @return the following execution node of the current node.
     * @throws IllegalStateException if  the next node is not present.
     */
    public static ExecutionNode ofOrThrow(ExecutionNode current, ExecutionGraph graph) {
        Collection<ExecutionNode> nextExecutorNodes = graph.successors(current);
        String currentNodeComponentName = current.getComponent().getClass().getName();
        return nextExecutorNodes.stream()
                .findFirst()
                .orElseThrow(() -> {
                    String errorMessage = format("Expected [%s] to have exactly one following node, but %d were found",
                            currentNodeComponentName, nextExecutorNodes.size());
                    return new IllegalStateException(errorMessage);
                });
    }
}
