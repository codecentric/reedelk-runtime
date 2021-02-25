package de.codecentric.reedelk.platform.component.commons;

import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.api.component.Component;
import de.codecentric.reedelk.runtime.api.component.Join;

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
