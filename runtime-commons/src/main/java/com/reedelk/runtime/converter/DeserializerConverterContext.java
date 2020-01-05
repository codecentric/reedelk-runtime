package com.reedelk.runtime.converter;

import com.reedelk.runtime.api.commons.ModuleContext;

public class DeserializerConverterContext {

    private final ModuleContext context;

    public DeserializerConverterContext(long moduleId) {
        this.context = new ModuleContext(moduleId);
    }

    public long getModuleId() {
        return context.getModuleId();
    }

    ModuleContext moduleContext() {
        return context;
    }
}
