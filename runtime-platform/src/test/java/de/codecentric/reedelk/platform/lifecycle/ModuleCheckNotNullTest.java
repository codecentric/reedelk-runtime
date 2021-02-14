package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Bundle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleCheckNotNullTest {

    @Mock
    private Bundle bundle;
    private ModuleCheckNotNull step;

    @BeforeEach
    void setUp() {
        step = Mockito.spy(new ModuleCheckNotNull());
    }

    @Test
    void shouldNotThrowExceptionWhenModuleIsNotNull() {
        // Given
        doReturn(bundle).when(step).bundle();
        Module module = mock(Module.class);

        // When
        Module actual = step.run(module);

        // Then
        assertThat(actual).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenModuleIsNull() {
        // Given
        doReturn(bundle).when(step).bundle();
        doReturn(23L).when(bundle).getBundleId();

        assertThrows(IllegalStateException.class, () -> step.run(null));
    }
}
