package com.reedelk.platform.flow;

import com.reedelk.platform.execution.FlowExecutorEngine;
import com.reedelk.platform.graph.ExecutionGraph;

public class ErrorStateFlow extends Flow {

    private final Exception exception;

    public ErrorStateFlow(long moduleId, String moduleName, String flowId, String flowTitle, ExecutionGraph executionGraph, FlowExecutorEngine executionEngine, Exception exception) {
        super(moduleId, moduleName, flowId, flowTitle, executionGraph, executionEngine);
        this.exception = exception;
    }

    public Exception getException() {
        return this.exception;
    }
}
