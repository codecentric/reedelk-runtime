package com.reedelk.esb.component.commons;

import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.component.Join;

import java.util.Optional;

public class JoinUtils {

    private JoinUtils() {
    }

    public static boolean isJoin(ExecutionNode nextAfterStop) {
        if (nextAfterStop != null) {
            Component joinComponent = nextAfterStop.getComponent();
            return joinComponent instanceof Join;
        }
        return false;
    }

    public static Join joinComponentOrDefault(ExecutionNode executionNode) {
        return Optional.ofNullable(executionNode)
                .flatMap(node -> node.getComponent() instanceof Join ?
                        Optional.of((Join) node.getComponent()) : Optional.empty())
                .orElse(new DefaultJoin());
    }
}
