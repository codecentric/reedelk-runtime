package com.reedelk.esb.test.utils;

import com.reedelk.esb.module.Module;

import static com.reedelk.esb.module.state.ModuleState.ERROR;
import static org.assertj.core.api.Assertions.assertThat;

public class Assertions {

    public static void assertModuleErrorStateWith(Module module, String expectedMessage) {
        assertThat(module.state()).isEqualTo(ERROR);
        boolean found = module.errors().stream()
                .anyMatch(exception -> expectedMessage.equals(exception.getMessage()));
        assertThat(found)
                .withFailMessage(String.format("Could not find expected error message '%s'", expectedMessage))
                .isTrue();
    }
}
