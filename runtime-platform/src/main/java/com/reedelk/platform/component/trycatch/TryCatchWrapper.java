package com.reedelk.platform.component.trycatch;

import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.runtime.component.TryCatch;

public class TryCatchWrapper extends TryCatch {

    private ExecutionNode stopNode;
    private ExecutionNode firstTryNode;
    private ExecutionNode firstCatchNode;

    public ExecutionNode getFirstTryNode() {
        return firstTryNode;
    }

    public void setFirstTryNode(ExecutionNode firstTryNode) {
        this.firstTryNode = firstTryNode;
    }

    public ExecutionNode getFirstCatchNode() {
        return firstCatchNode;
    }

    public void setFirstCatchNode(ExecutionNode firstCatchNode) {
        this.firstCatchNode = firstCatchNode;
    }

    public ExecutionNode getStopNode() {
        return stopNode;
    }

    public void setStopNode(ExecutionNode stopNode) {
        this.stopNode = stopNode;
    }
}
