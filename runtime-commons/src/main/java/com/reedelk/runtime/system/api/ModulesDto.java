package com.reedelk.runtime.system.api;

import java.util.Collection;
import java.util.HashSet;

public class ModulesDto {

    private Collection<ModuleDto> moduleDtos = new HashSet<>();

    public Collection<ModuleDto> getModuleDtos() {
        return moduleDtos;
    }

    public void setModuleDtos(Collection<ModuleDto> moduleDtos) {
        this.moduleDtos = moduleDtos;
    }
}
