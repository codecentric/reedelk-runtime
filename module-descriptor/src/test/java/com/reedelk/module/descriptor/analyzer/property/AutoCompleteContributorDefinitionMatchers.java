package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.model.AutoCompleteContributorDescriptor;

import java.util.List;

public class AutoCompleteContributorDefinitionMatchers {

    public interface AutoCompleteContributorDefinitionMatcher {
        boolean matches(AutoCompleteContributorDescriptor actual);
    }

    public static AutoCompleteContributorDefinitionMatcher with(boolean isMessage, boolean isContext, boolean isError, List<String> customContributions) {
        return actual -> {
            boolean actualIsMessage = actual.isMessage();
            boolean actualIsContext = actual.isContext();
            boolean actualIsError = actual.isError();
            List<String> actualContributions = actual.getContributions();
            return isMessage == actualIsMessage &&
                    isContext == actualIsContext &&
                    isError == actualIsError &&
                    customContributions.equals(actualContributions);
        };
    }
}
