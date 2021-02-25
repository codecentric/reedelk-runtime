package de.codecentric.reedelk.runtime.api.exception;

public class FlowExecutionException extends PlatformException {

    private final long moduleId;
    private final String moduleName;
    private final String flowId;
    private final String flowTitle;
    private final String correlationId;

    public FlowExecutionException(long moduleId,
                                  String moduleName,
                                  String flowId,
                                  String flowTitle,
                                  String correlationId,
                                  String errorMessage,
                                  Throwable exception) {
        super(errorMessage, exception);
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.flowId = flowId;
        this.flowTitle = flowTitle;
        this.correlationId = correlationId;
    }

    public long getModuleId() {
        return moduleId;
    }

    public String getFlowId() {
        return flowId;
    }

    public String getFlowTitle() {
        return flowTitle;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getCorrelationId() {
        return correlationId;
    }
}
