package de.codecentric.reedelk.platform.component.foreach;

import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.component.ForEach;

public class ForEachWrapper extends ForEach {

    private ExecutionNode stopNode;
    private ExecutionNode firstEachNode;

    public ExecutionNode getStopNode() {
        return stopNode;
    }

    public ExecutionNode getFirstEachNode() {
        return firstEachNode;
    }

    public void setStopNode(ExecutionNode stopNode) {
        this.stopNode = stopNode;
    }

    public void setFirstEachNode(ExecutionNode firstEachNode) {
        this.firstEachNode = firstEachNode;
    }
}
