package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.model.ScriptSignatureDescriptor;

import java.util.List;

public class ScriptSignatureDefinitionMatchers {

    public interface ScriptSignatureDefinitionMatcher {
        boolean matches(ScriptSignatureDescriptor actual);
    }

    public static ScriptSignatureDefinitionMatcher with(List<String> expectedArguments) {
        return actual -> expectedArguments.equals(actual.getArguments());
    }
}
