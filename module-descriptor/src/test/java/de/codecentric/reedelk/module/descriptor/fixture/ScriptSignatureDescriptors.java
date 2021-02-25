package de.codecentric.reedelk.module.descriptor.fixture;

import de.codecentric.reedelk.module.descriptor.model.property.ScriptSignatureArgument;
import de.codecentric.reedelk.module.descriptor.model.property.ScriptSignatureDescriptor;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;

import java.util.Map;

import static java.util.Arrays.asList;

public class ScriptSignatureDescriptors {

    private ScriptSignatureDescriptors() {
    }

    public static final ScriptSignatureDescriptor scriptSignatureDescriptorDefault = newScriptSignatureDescriptor();

    private static ScriptSignatureDescriptor newScriptSignatureDescriptor() {
        ScriptSignatureDescriptor signatureDescriptor = new ScriptSignatureDescriptor();
        signatureDescriptor.setArguments(
                asList(new ScriptSignatureArgument("input", Map.class.getName()),
                        new ScriptSignatureArgument("output", FlowContext.class.getName())));
        return signatureDescriptor;
    }
}
