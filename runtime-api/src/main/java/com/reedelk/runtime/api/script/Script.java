package com.reedelk.runtime.api.script;

import com.reedelk.runtime.api.commons.FunctionNameUUID;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.commons.ScriptUtils;

public class Script implements ScriptBlock {

    private final ModuleContext context;
    private final String scriptFile;
    private final String functionNameUUID;

    public static Script from(String scriptFile, ModuleContext context) {
        if (context == null) {
            throw new IllegalArgumentException("context");
        }
        return new Script(scriptFile, context);
    }

    protected Script(String scriptFile, ModuleContext context) {
        this.context = context;
        this.scriptFile = scriptFile;
        this.functionNameUUID = FunctionNameUUID.generate(context);
    }

    @Override
    public String functionName() {
        return functionNameUUID;
    }

    @Override
    public boolean isEmpty() {
        return ScriptUtils.isBlank(body());
    }

    @Override
    public String body() {
        throw new UnsupportedOperationException("implemented in the proxy");
    }

    public ModuleContext getContext() {
        return context;
    }

    public String getScriptPath() {
        return scriptFile;
    }
}
