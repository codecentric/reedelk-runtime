package com.reedelk.platform.lifecycle;

import com.reedelk.platform.module.Module;
import com.reedelk.platform.module.ModulesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleAddTest {

    @Mock
    private ModulesManager modulesManager;

    private ModuleAdd step;

    @BeforeEach
    void setUp() {
        step = spy(new ModuleAdd());
    }

    @Test
    void shouldAddModuleToManager() {
        // Given
        doReturn(modulesManager).when(step).modulesManager();
        Module module = mock(Module.class);

        // When
        step.run(module);

        // Then
        verify(modulesManager).add(module);
        verifyNoMoreInteractions(modulesManager);
    }
}
