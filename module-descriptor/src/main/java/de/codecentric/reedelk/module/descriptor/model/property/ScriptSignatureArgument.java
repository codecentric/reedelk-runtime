package de.codecentric.reedelk.module.descriptor.model.property;

import java.io.Serializable;

public class ScriptSignatureArgument implements Serializable {

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

    @Override
    public String toString() {
        return "ScriptSignatureArgument{" +
                "argumentName='" + argumentName + '\'' +
                ", argumentType='" + argumentType + '\'' +
                '}';
    }
}
