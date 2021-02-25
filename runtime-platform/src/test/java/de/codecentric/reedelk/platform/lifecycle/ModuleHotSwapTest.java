package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.platform.module.ModuleDeserializer;
import de.codecentric.reedelk.platform.module.deserializer.FileSystemDeserializer;
import de.codecentric.reedelk.platform.module.state.ModuleState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.introspection.FieldSupport.EXTRACTION;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class ModuleHotSwapTest {

    private final Void VOID = null;

    @Mock
    private Bundle bundle;

    private ModuleHotSwap step;

    @BeforeEach
    void setUp() {
        step = Mockito.spy(new ModuleHotSwap("/Users/test/dir"));
    }

    @Test
    void shouldCreateModuleWithCorrectParametersAndBundleDeserializer() {
        // Given
        doReturn(bundle).when(step).bundle();

        doReturn(342L).when(bundle).getBundleId();
        doReturn(new Version("1.1.0")).when(bundle).getVersion();
        doReturn("hotswap-bundle").when(bundle).getSymbolicName();
        doReturn("file:/usr/local/desktop/my-hotswap-1.1.0-SNAPSHOT.jar").when(bundle).getLocation();

        // When
        Module created = step.run(VOID);

        // Then
        assertThat(created.id()).isEqualTo(342L);
        assertThat(created.state()).isEqualTo(ModuleState.INSTALLED);
        assertThat(created.version()).isEqualTo("1.1.0");
        assertThat(created.name()).isEqualTo("hotswap-bundle");
        assertThat(created.filePath()).isEqualTo("file:/usr/local/desktop/my-hotswap-1.1.0-SNAPSHOT.jar");

        ModuleDeserializer deserializer = EXTRACTION.fieldValue("deserializer", ModuleDeserializer.class, created);
        assertThat(deserializer).isInstanceOf(FileSystemDeserializer.class);
    }
}
