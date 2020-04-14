package com.reedelk.platform.lifecycle;

import com.reedelk.platform.module.Module;
import com.reedelk.platform.module.ModuleDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static com.reedelk.platform.module.state.ModuleState.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ModuleUpdateUnregisteredComponentTest {

    private final long moduleId = 232L;
    private final String testModuleName = "TestModule";
    private final String testVersion = "0.9.0";
    private final String testLocation = "file://location/test";

    private final String component1 = "com.reedelk.platform.AwesomeComponent1";
    private final String component2 = "com.reedelk.platform.AwesomeComponent2";
    private final String component3 = "com.reedelk.platform.AwesomeComponent3";
    private final String component4 = "com.reedelk.platform.AwesomeComponent4";

    @Mock
    private ModuleDeserializer deserializer;

    @Test
    void shouldRemoveComponentFromResolvedModule() {
        // Given
        Collection<String> unresolvedComponents = emptyList();
        Collection<String> resolvedComponents = asList(component2, component4);

        Module module = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();

        module.unresolve(unresolvedComponents, resolvedComponents);
        module.resolve(resolvedComponents);

        // When (component 4 is removed from the system)
        ModuleUpdateUnregisteredComponent step = new ModuleUpdateUnregisteredComponent(component4);
        Module unresolvedModule = step.run(module);

        // Then
        assertThat(unresolvedModule.state()).isEqualTo(UNRESOLVED);
        assertThat(unresolvedModule.unresolvedComponents()).containsExactlyInAnyOrder(component4);
        assertThat(unresolvedModule.resolvedComponents()).containsExactlyInAnyOrder(component2);
    }

    @Test
    void shouldRemoveComponentFromAlreadyUnresolvedModule() {
        // Given
        Collection<String> unresolvedComponents = asList(component3, component1);
        Collection<String> resolvedComponents = asList(component2, component4);

        Module module = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        module.unresolve(unresolvedComponents, resolvedComponents);

        // When (component 4 is removed from the system)
        ModuleUpdateUnregisteredComponent step = new ModuleUpdateUnregisteredComponent(component4);
        Module unresolvedModule = step.run(module);

        // Then
        assertThat(unresolvedModule.state()).isEqualTo(UNRESOLVED);
        assertThat(unresolvedModule.unresolvedComponents()).containsExactlyInAnyOrder(component3, component1, component4);
        assertThat(unresolvedModule.resolvedComponents()).containsExactly(component2);
    }

    @Test
    void shouldTransitionErrorStateModuleToUnresolved() {
        // Given
        Module module = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();

        Collection<String> unresolvedComponents = emptyList();
        Collection<String> resolvedComponents = asList(component2, component4);
        module.unresolve(unresolvedComponents, resolvedComponents);
        module.resolve(resolvedComponents);
        module.error(new Exception());

        // When
        ModuleUpdateUnregisteredComponent step = new ModuleUpdateUnregisteredComponent(component4);
        Module unresolvedModule = step.run(module);

        // Then
        assertThat(unresolvedModule.state()).isEqualTo(UNRESOLVED);
    }

    @Test
    void shouldThrowExceptionWhenStateIsInstalled() {
        // Given
        Module module = mock(Module.class);
        doReturn(INSTALLED).when(module).state();

        // Expect
        assertThrows(IllegalStateException.class, () -> {
            // When
            ModuleUpdateUnregisteredComponent step = new ModuleUpdateUnregisteredComponent(component4);
            step.run(module);
        });
    }

    @Test
    void shouldThrowExceptionWhenStateIsStarted() {
        // Given
        Module module = mock(Module.class);
        doReturn(STARTED).when(module).state();

        // Expect
        assertThrows(IllegalStateException.class, () -> {
            // When
            ModuleUpdateUnregisteredComponent step = new ModuleUpdateUnregisteredComponent(component4);
            step.run(module);
        });
    }

    @Test
    void shouldThrowExceptionWhenStateIsStopped() {
        // Given
        Module module = mock(Module.class);
        doReturn(STOPPED).when(module).state();

        // Expect
        assertThrows(IllegalStateException.class, () -> {
            // When
            ModuleUpdateUnregisteredComponent step = new ModuleUpdateUnregisteredComponent(component4);
            step.run(module);
        });
    }
}
