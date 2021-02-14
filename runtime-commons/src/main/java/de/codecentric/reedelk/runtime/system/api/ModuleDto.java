package de.codecentric.reedelk.runtime.system.api;

import java.util.Collection;
import java.util.Collections;

public class ModuleDto {

    private String name;
    private String state;
    private long moduleId;
    private String version;
    private String moduleFilePath;

    private Collection<FlowDto> flows = Collections.emptyList();
    private Collection<ErrorDto> errors = Collections.emptyList();
    private Collection<String> resolvedComponents = Collections.emptyList();
    private Collection<String> unresolvedComponents = Collections.emptyList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModuleFilePath() {
        return moduleFilePath;
    }

    public void setModuleFilePath(String moduleFilePath) {
        this.moduleFilePath = moduleFilePath;
    }

    public Collection<FlowDto> getFlows() {
        return flows;
    }

    public void setFlows(Collection<FlowDto> flows) {
        this.flows = flows;
    }

    public Collection<ErrorDto> getErrors() {
        return errors;
    }

    public void setErrors(Collection<ErrorDto> errors) {
        this.errors = errors;
    }

    public Collection<String> getResolvedComponents() {
        return resolvedComponents;
    }

    public void setResolvedComponents(Collection<String> resolvedComponents) {
        this.resolvedComponents = resolvedComponents;
    }

    public Collection<String> getUnresolvedComponents() {
        return unresolvedComponents;
    }

    public void setUnresolvedComponents(Collection<String> unresolvedComponents) {
        this.unresolvedComponents = unresolvedComponents;
    }
}
