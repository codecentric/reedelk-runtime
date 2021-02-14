package de.codecentric.reedelk.module.descriptor.model.property;

import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

import java.io.Serializable;
import java.util.List;

import static java.util.Arrays.asList;

public class ScriptSignatureDescriptor implements Serializable {

    public static final ScriptSignatureDescriptor DEFAULT = new ScriptSignatureDescriptor(
            asList(new ScriptSignatureArgument("context", FlowContext.class.getName()),
                    new ScriptSignatureArgument("message", Message.class.getName())));

    private List<ScriptSignatureArgument> arguments;

    public ScriptSignatureDescriptor() {
    }

    public ScriptSignatureDescriptor(List<ScriptSignatureArgument> arguments) {
        this.arguments = arguments;
    }

    public List<ScriptSignatureArgument> getArguments() {
        return arguments;
    }

    public void setArguments(List<ScriptSignatureArgument> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "ScriptSignatureDescriptor{" +
                "arguments=" + arguments +
                '}';
    }
}
