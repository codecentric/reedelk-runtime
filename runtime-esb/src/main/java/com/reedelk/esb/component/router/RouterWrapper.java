package com.reedelk.esb.component.router;

import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.exception.PlatformException;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import com.reedelk.runtime.component.Router;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class RouterWrapper extends Router {

    private List<PathExpressionPair> pathExpressionPairs = new ArrayList<>();
    private ExecutionNode endOfRouterStopNode;

    /**
     * Returns all paths without the default one.
     */
    public List<PathExpressionPair> getPathExpressionPairs() {
        return pathExpressionPairs.stream()
                .filter(pathExpressionPair -> !DEFAULT_CONDITION.equals(pathExpressionPair.expression))
                .collect(toList());
    }

    public void addExpressionAndPathPair(DynamicString expression, ExecutionNode pathExecutionNode) {
        pathExpressionPairs.add(new PathExpressionPair(expression, pathExecutionNode));
    }

    public PathExpressionPair getDefaultPathOrThrow() {
        return pathExpressionPairs.stream()
                .filter(pathExpressionPair -> DEFAULT_CONDITION.value().equals(pathExpressionPair.expression.value()))
                .findFirst()
                .orElseThrow(() -> new PlatformException("Default router condition could not be found"));
    }

    public void setEndOfRouterStopNode(ExecutionNode endOfRouterStopNode) {
        this.endOfRouterStopNode = endOfRouterStopNode;
    }

    public ExecutionNode getEndOfRouterStopNode() {
        return endOfRouterStopNode;
    }

    public static class PathExpressionPair {
        public final DynamicString expression;
        public final ExecutionNode pathReference;

        PathExpressionPair(DynamicString expression, ExecutionNode pathReference) {
            this.expression = expression;
            this.pathReference = pathReference;
        }
    }
}
