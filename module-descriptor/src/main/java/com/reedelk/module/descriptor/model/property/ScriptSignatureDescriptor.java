package com.reedelk.module.descriptor.model.property;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

import java.io.Serializable;
import java.util.List;

import static java.util.Arrays.asList;

public class ScriptSignatureDescriptor implements Serializable {

    public static final ScriptSignatureDescriptor DEFAULT =
            new ScriptSignatureDescriptor(asList("context", "message"),
                    asList(FlowContext.class.getName(), Message.class.getName()));

    private List<String> arguments;
    private List<String> types;

    public ScriptSignatureDescriptor() {
    }

    public ScriptSignatureDescriptor(List<String> arguments, List<String> types) {
        this.arguments = arguments;
        this.types = types;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "ScriptSignatureDescriptor{" +
                "arguments=" + arguments +
                ", types=" + types +
                '}';
    }
}
