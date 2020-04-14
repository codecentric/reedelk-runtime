package com.reedelk.platform.component.fork;

import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.runtime.component.Fork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForkWrapper extends Fork {

    private List<ExecutionNode> forkNodes = new ArrayList<>();

    private ExecutionNode stopNode;

    public ExecutionNode getStopNode() {
        return this.stopNode;
    }

    public void setStopNode(ExecutionNode stopNode) {
        this.stopNode = stopNode;
    }

    public void addForkNode(ExecutionNode executionNode) {
        this.forkNodes.add(executionNode);
    }

    public List<ExecutionNode> getForkNodes() {
        return Collections.unmodifiableList(forkNodes);
    }
}
