package de.codecentric.reedelk.runtime.rest.api.module.v1;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class ModulesGETRes {

    private Collection<ModuleGETRes> modules = new HashSet<>();

    public Collection<ModuleGETRes> getModules() {
        return modules;
    }

    public void setModules(Collection<ModuleGETRes> modules) {
        this.modules = Collections.unmodifiableCollection(modules);
    }

}
