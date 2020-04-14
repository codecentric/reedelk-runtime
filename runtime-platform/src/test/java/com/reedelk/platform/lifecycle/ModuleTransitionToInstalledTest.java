package com.reedelk.platform.lifecycle;

import com.reedelk.platform.module.Module;
import com.reedelk.platform.module.ModuleDeserializer;
import com.reedelk.platform.module.state.ModuleState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.reedelk.platform.module.state.ModuleState.INSTALLED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleTransitionToInstalledTest {

    @Mock
    private ModuleDeserializer deserializer;

    private ModuleTransitionToInstalled step;

    @BeforeEach
    void setUp() {
        step = spy(new ModuleTransitionToInstalled());
    }

    @Test
    void shouldTransitionModuleToInstalledState() {
        // Given
        Module inputModule = spy(Module.builder()
                .moduleId(33L)
                .name("StopTestModule")
                .version("0.9.0")
                .deserializer(deserializer)
                .moduleFilePath("file://location/test")
                .build());
        inputModule.unresolve(Collections.emptyList(), Collections.emptyList());
        inputModule.resolve(Collections.emptyList());

        assumeTrue(inputModule.state() == ModuleState.RESOLVED, "Expected module to be in resolved state");

        // When
        Module actualModule = step.run(inputModule);

        // Then
        verify(inputModule).installed();
        assertThat(actualModule.state()).isEqualTo(INSTALLED);
    }

    @Test
    void shouldNotDoAnythingIfStateIsAlreadyInstalled() {
        // Given
        Module inputModule = spy(Module.builder()
                .moduleId(33L)
                .name("StopTestModule")
                .version("0.9.0")
                .deserializer(deserializer)
                .moduleFilePath("file://location/test")
                .build());

        assumeTrue(inputModule.state() == INSTALLED, "Expected module to be in 'installed' state");

        // When
        Module actualModule = step.run(inputModule);

        // Then
        verify(inputModule, never()).installed();
        assertThat(actualModule.state()).isEqualTo(INSTALLED);
    }
}
