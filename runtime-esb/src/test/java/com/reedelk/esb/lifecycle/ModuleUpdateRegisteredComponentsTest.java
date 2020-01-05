package com.reedelk.esb.lifecycle;

import com.reedelk.esb.component.ComponentRegistry;
import com.reedelk.esb.module.Module;
import com.reedelk.esb.module.ModuleDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static com.reedelk.esb.module.state.ModuleState.RESOLVED;
import static com.reedelk.esb.module.state.ModuleState.UNRESOLVED;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleUpdateRegisteredComponentsTest {

    private final long moduleId = 232L;
    private final String testModuleName = "TestModule";
    private final String testVersion = "0.9.0";
    private final String testLocation = "file://location/test";

    private final String component1 = "com.reedelk.esb.testing.AwesomeComponent1";
    private final String component2 = "com.reedelk.esb.testing.AwesomeComponent2";
    private final String component3 = "com.reedelk.esb.testing.AwesomeComponent3";
    private final String component4 = "com.reedelk.esb.testing.AwesomeComponent4";

    @Mock
    private ModuleDeserializer deserializer;
    @Mock
    private ComponentRegistry componentRegistry;

    private ModuleUpdateRegisteredComponents step;

    @BeforeEach
    void setUp() {
        step = spy(new ModuleUpdateRegisteredComponents());
        doReturn(componentRegistry).when(step).componentRegistry();
    }

    @Test
    void shouldTransitionToResolved() {
        // Given
        doReturn(emptyList())
                .when(componentRegistry)
                .unregisteredComponentsOf(anyCollection());

        Collection<String> unresolvedComponents = asList(component1, component3);
        Collection<String> resolvedComponents = asList(component2, component4);

        Module module = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        module.unresolve(unresolvedComponents, resolvedComponents);

        // When
        Module updatedModule = step.run(module);

        // Then
        assertThat(updatedModule.state()).isEqualTo(RESOLVED);
        assertThat(updatedModule.resolvedComponents())
                .containsExactlyInAnyOrder(component1, component2, component3, component4);
    }

    @Test
    void shouldKeepModuleUnresolvedAndUpdateResolvedAndUnresolvedComponents() {
        // Given
        doReturn(singletonList(component1))
                .when(componentRegistry)
                .unregisteredComponentsOf(anyCollection());

        Collection<String> unresolvedComponents = asList(component1, component3);
        Collection<String> resolvedComponents = asList(component2, component4);

        Module module = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        module.unresolve(unresolvedComponents, resolvedComponents);

        // When
        Module updatedModule = step.run(module);

        // Then
        assertThat(updatedModule.state()).isEqualTo(UNRESOLVED);
        assertThat(updatedModule.resolvedComponents()).containsExactlyInAnyOrder(component2, component3, component4);
        assertThat(updatedModule.unresolvedComponents()).containsExactly(component1);
    }
}