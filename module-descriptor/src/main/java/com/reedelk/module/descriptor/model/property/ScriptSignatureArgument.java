package com.reedelk.module.descriptor.model.property;

public class ScriptSignatureArgument {

    private final String argumentName;
    private final String argumentType;

    public ScriptSignatureArgument(String argumentName, String argumentType) {
        this.argumentName = argumentName;
        this.argumentType = argumentType;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public String getArgumentType() {
        return argumentType;
    }
}
