package com.reedelk.platform.flow.deserializer.converter;

import com.reedelk.runtime.api.script.Script;

public class ProxyScript extends Script {

    private final String scriptBody;

    public ProxyScript(Script original, String scriptBody) {
        super(original.getScriptPath(), original.getContext());
        this.scriptBody = scriptBody;
    }

    @Override
    public String body() {
        return scriptBody;
    }
}
