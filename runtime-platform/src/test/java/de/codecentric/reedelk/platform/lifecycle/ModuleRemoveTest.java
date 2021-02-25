package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.platform.module.ModuleDeserializer;
import de.codecentric.reedelk.platform.module.ModulesManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleRemoveTest {

    @Mock
    private ModulesManager modulesManager;
    @Mock
    private ModuleDeserializer deserializer;

    private ModuleRemove step;

    @BeforeEach
    void setUp() {
        step = Mockito.spy(new ModuleRemove());
    }

    @Test
    void shouldRemoveModuleFormModulesManager() {
        // Given
        doReturn(modulesManager).when(step).modulesManager();

        String testVersion = "0.9.0";
        String testLocation = "file://location/test";
        Module module = Module.builder()
                .moduleId(14L)
                .name("TestModule")
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();

        // When
        step.run(module);

        // Then
        verify(modulesManager).removeModuleById(module.id());
        verifyNoMoreInteractions(modulesManager);
    }

    @Test
    void shouldRemoveModuleFormModulesManagerThrowExceptionWhenModuleStillStarted() {
        // Given
        String testVersion = "0.9.0";
        String testLocation = "file://location/test";
        Module module = Module.builder()
                .moduleId(14L)
                .name("TestModule")
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();

        module.unresolve(emptyList(), emptyList());
        module.resolve(emptyList());
        module.stop(emptySet());
        module.start(emptySet());

        // Expect
        IllegalStateException thrown =
                Assertions.assertThrows(IllegalStateException.class, () -> step.run(module));

        org.assertj.core.api.Assertions.assertThat(thrown.getMessage())
                .contains("Module with id=[14], name=[TestModule] could not be removed: its state is [STARTED]");
    }
}
