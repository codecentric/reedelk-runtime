package de.codecentric.reedelk.runtime.api.commons;

public class FlowError {

    // These properties *MUST* be in sync with the fields below.
    // Otherwise we break compatibility between plugin and runtime.
    public interface Properties {
        String moduleId = "moduleId";
        String moduleName = "moduleName";
        String flowId = "flowId";
        String flowTitle = "flowTitle";
        String correlationId = "correlationId";
        String errorType = "errorType";
        String errorMessage = "errorMessage";
    }

    public final long moduleId;
    public final String moduleName;
    public final String flowId;
    public final String flowTitle;
    public final String correlationId;
    public final String errorType;
    public final String errorMessage;

    public FlowError(long moduleId, String moduleName, String flowId, String flowTitle, String correlationId, String errorType, String errorMessage) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.flowId = flowId;
        this.flowTitle = flowTitle;
        this.correlationId = correlationId;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public long getModuleId() {
        return moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getFlowId() {
        return flowId;
    }

    public String getFlowTitle() {
        return flowTitle;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
