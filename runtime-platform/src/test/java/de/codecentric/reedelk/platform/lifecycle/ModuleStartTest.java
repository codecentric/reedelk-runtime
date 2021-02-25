package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.flow.Flow;
import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.platform.module.state.ModuleState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ModuleStartTest {

    @Mock
    private Flow flow2;
    @Mock
    private Flow flow1;
    @Mock
    private Bundle bundle;
    @Mock
    private BundleContext bundleContext;
    @Mock
    private Module module;

    private ModuleStart step;

    @Captor
    private ArgumentCaptor<Collection<Flow>> flowsCaptor;
    @Captor
    private ArgumentCaptor<Collection<Exception>> errorsCaptor;

    @BeforeEach
    void setUp() {
        step = Mockito.spy(new ModuleStart());
        doReturn(bundle).when(step).bundle();
        doReturn(bundleContext).when(bundle).getBundleContext();
        doReturn(asList(flow1, flow2)).when(module).flows();
    }

    @Test
    void shouldNotStartGivenStateInstalled() {
        // Given
        Mockito.doReturn(ModuleState.INSTALLED)
                .when(module)
                .state();

        // When
        Module outModule = step.run(module);

        // Then
        assertThat(outModule).isEqualTo(module);

        verify(module).state();
        verifyNoMoreInteractions(module);
    }

    @Test
    void shouldNotStartGivenStateUnresolved() {
        // Given
        Mockito.doReturn(ModuleState.UNRESOLVED)
                .when(module)
                .state();

        // When
        Module outModule = step.run(module);

        // Then
        assertThat(outModule).isEqualTo(module);

        verify(module).state();
        verifyNoMoreInteractions(module);
    }

    @Test
    void shouldNotStartGivenStateResolved() {
        // Given
        Mockito.doReturn(ModuleState.RESOLVED)
                .when(module)
                .state();

        // When
        Module outModule = step.run(module);

        // Then
        assertThat(outModule).isEqualTo(module);

        verify(module).state();
        verifyNoMoreInteractions(module);
    }

    @Test
    void shouldNotStartGivenStateStarted() {
        // Given
        Mockito.doReturn(ModuleState.STARTED)
                .when(module)
                .state();

        // When
        Module outModule = step.run(module);

        // Then
        assertThat(outModule).isEqualTo(module);

        verify(module).state();
        verifyNoMoreInteractions(module);
    }

    @Test
    void shouldNotStartGivenStateError() {
        // Given
        Mockito.doReturn(ModuleState.ERROR)
                .when(module)
                .state();

        // When
        Module outModule = step.run(module);

        // Then
        assertThat(outModule).isEqualTo(module);

        verify(module).state();
        verifyNoMoreInteractions(module);
    }

    @Test
    void shouldCorrectlyStartGivenStateStopped() {
        // Given
        Mockito.doReturn(ModuleState.STOPPED)
                .when(module)
                .state();

        // When
        Module outModule = step.run(module);

        // Then
        assertThat(outModule).isEqualTo(module);

        verify(flow1).start();
        verify(flow2).start();
        verify(module).flows();
        verify(module).state();
        verify(module).start(flowsCaptor.capture());

        verifyNoMoreInteractions(module);

        Collection<Flow> flowsPassed = flowsCaptor.getValue();
        assertThat(flowsPassed).hasSize(2);
        assertThat(flowsPassed).containsExactlyInAnyOrder(flow1, flow2);
    }

    @Test
    void shouldTransitionToErrorStateWhenErrorWhileStartingFlows() {
        // Given
        Mockito.doReturn(ModuleState.STOPPED)
                .when(module)
                .state();

        doThrow(new RuntimeException("error x.y.z while starting flow"))
                .when(flow2)
                .start();
        doReturn("aabbccddee")
                .when(flow2)
                .getFlowId();

        // When
        Module outModule = step.run(module);

        // Then
        assertThat(outModule).isEqualTo(module);

        verify(module).flows();
        verify(module).state();
        verify(module).error(errorsCaptor.capture());
        verify(module).id(); // Used in error message format
        verify(module).name(); // Used in error message format

        verifyNoMoreInteractions(module);

        // Verify flows are started, then stopped and references release (because of the error while starting)
        verify(flow1).start();
        verify(flow2).start();
        verify(flow1).forceStop();
        verify(flow2).forceStop();
        verify(flow1).releaseReferences(bundle);
        verify(flow2).releaseReferences(bundle);


        Collection<Exception> errors = errorsCaptor.getValue();
        assertThat(errors).hasSize(1);

        String expectedErrorMessage = "{\n" +
                "  \"errorMessage\": \"error x.y.z while starting flow\",\n" +
                "  \"moduleId\": 0,\n" +
                "  \"flowId\": \"aabbccddee\",\n" +
                "  \"errorType\": \"java.lang.RuntimeException\"\n" +
                "}";
        assertThat(errors.iterator().next().getMessage()).isEqualTo(expectedErrorMessage);
    }
}
