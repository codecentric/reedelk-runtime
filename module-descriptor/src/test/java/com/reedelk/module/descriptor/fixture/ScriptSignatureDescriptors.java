package com.reedelk.module.descriptor.fixture;

import com.reedelk.module.descriptor.model.script.ScriptSignatureDescriptor;

import java.util.Arrays;

public class ScriptSignatureDescriptors {

    private ScriptSignatureDescriptors() {
    }

    public static final ScriptSignatureDescriptor scriptSignatureDescriptorDefault = newScriptSignatureDescriptor();

    private static ScriptSignatureDescriptor newScriptSignatureDescriptor() {
        ScriptSignatureDescriptor signatureDescriptor = new ScriptSignatureDescriptor();
        signatureDescriptor.setArguments(Arrays.asList("input","output"));
        return signatureDescriptor;
    }
}
