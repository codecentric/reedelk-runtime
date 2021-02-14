package de.codecentric.reedelk.platform.test.utils;

import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.platform.module.state.ModuleState;

import static org.assertj.core.api.Assertions.assertThat;

public class Assertions {

    public static void assertModuleErrorStateWith(Module module, String expectedMessage) {
        assertThat(module.state()).isEqualTo(ModuleState.ERROR);
        boolean found = module.errors().stream()
                .anyMatch(exception -> expectedMessage.equals(exception.getMessage()));
        assertThat(found)
                .withFailMessage(String.format("Could not find expected error message '%s'", expectedMessage))
                .isTrue();
    }
}
