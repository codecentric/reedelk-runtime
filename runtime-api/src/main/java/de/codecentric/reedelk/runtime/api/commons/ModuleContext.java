package de.codecentric.reedelk.runtime.api.commons;


public class ModuleContext {

    private final long moduleId;

    public ModuleContext(long moduleId) {
        this.moduleId = moduleId;
    }

    public long getModuleId() {
        return moduleId;
    }
}
