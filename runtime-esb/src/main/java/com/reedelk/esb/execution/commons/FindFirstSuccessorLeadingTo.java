package com.reedelk.esb.execution.commons;

import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.Component;

import java.util.Collection;

import static com.reedelk.esb.commons.Messages.Execution.ERROR_FIRST_SUCCESSOR_LEADING_TO_END;

public class FindFirstSuccessorLeadingTo {

    private FindFirstSuccessorLeadingTo() {
    }

    public static ExecutionNode of(ExecutionGraph graph, ExecutionNode start, ExecutionNode end) {
        Collection<ExecutionNode> successors = graph.successors(start);
        for (ExecutionNode firstSuccessorOfStart : successors) {
            if (existsPathLeadingToEnd(graph, firstSuccessorOfStart, graph.successors(firstSuccessorOfStart), end)) {
                // We found the first successor leading to 'end'  execution node.
                return firstSuccessorOfStart;
            }
        }

        Component startComponent = start.getComponent();
        Component endComponent = end.getComponent();
        String error = ERROR_FIRST_SUCCESSOR_LEADING_TO_END.format(startComponent.getClass().getName(), endComponent.getClass().getName());
        throw new IllegalStateException(error);
    }

    private static boolean existsPathLeadingToEnd(ExecutionGraph graph, ExecutionNode root, Collection<ExecutionNode> successors, ExecutionNode end) {
        return root == end ||
                successors.contains(end) ||
                successors.stream()
                        .anyMatch(executionNode ->
                                existsPathLeadingToEnd(graph, root, graph.successors(executionNode), end));
    }
}
