package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.model.property.ScriptSignatureDescriptor;

public class ScriptSignatureDefinitionMatchers {

    public interface ScriptSignatureDefinitionMatcher {
        boolean matches(ScriptSignatureDescriptor actual);
    }

    public static ScriptSignatureDefinitionMatcher with(ScriptSignatureDescriptor expectedDescriptor) {
        return actualDescriptor -> {
            String expected = expectedDescriptor.toString();
            String actual = actualDescriptor.toString();
            return expected.equals(actual);
        };
    }
}
