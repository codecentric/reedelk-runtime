package com.reedelk.module.descriptor.model;

import java.io.Serializable;
import java.util.List;

import static java.util.Arrays.asList;

public class ScriptSignatureDescriptor implements Serializable {

    public static final ScriptSignatureDescriptor DEFAULT =
            new ScriptSignatureDescriptor(asList("context", "message"));

    private List<String> arguments;

    public ScriptSignatureDescriptor() {
    }

    private ScriptSignatureDescriptor(List<String> arguments) {
        this.arguments = arguments;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "ScriptSignatureDescriptor{" +
                "arguments=" + arguments +
                '}';
    }
}
