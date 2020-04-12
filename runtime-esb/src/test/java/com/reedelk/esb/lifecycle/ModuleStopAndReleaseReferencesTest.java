package com.reedelk.esb.lifecycle;

import com.reedelk.esb.flow.Flow;
import com.reedelk.esb.module.Module;
import com.reedelk.esb.module.ModuleDeserializer;
import com.reedelk.runtime.api.exception.PlatformException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.util.Collection;

import static com.reedelk.esb.module.state.ModuleState.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ModuleStopAndReleaseReferencesTest {

    private final long moduleId = 33L;
    private final String testModuleName = "StopTestModule";
    private final String testVersion = "0.9.0";
    private final String testLocation = "file://location/test";

    private final Collection<String> unresolvedComponents = asList("com.reedelk.esb.UnresolvedComponent1", "com.reedelk.esb.UnresolvedComponent1");
    private final Collection<String> resolvedComponents = asList("com.reedelk.esb.ResolvedComponent1", "com.reedelk.esb.ResolvedComponent2");

    @Mock
    private Flow flow1;
    @Mock
    private Flow flow2;
    @Mock
    private Flow flow3;
    @Mock
    private Bundle bundle;
    @Mock
    private BundleContext context;
    @Mock
    private ModuleDeserializer deserializer;

    private ModuleStopAndReleaseReferences step;

    @BeforeEach
    void setUp() {
        step = spy(new ModuleStopAndReleaseReferences());
        doReturn(bundle).when(step).bundle();
        doReturn(context).when(bundle).getBundleContext();
    }

    @Test
    void shouldNotStopModuleWhenStateIsInstalled() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();

        // When
        Module actualModule = step.run(inputModule);

        // Then
        assertThat(actualModule.state()).isEqualTo(INSTALLED);
    }

    @Test
    void shouldNotStopModuleWhenStateIsUnresolved() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);

        // When
        Module actualModule = step.run(inputModule);

        // Then
        assertThat(actualModule.state()).isEqualTo(UNRESOLVED);
    }

    @Test
    void shouldNotStopModuleWhenStateIsResolve() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);
        inputModule.resolve(resolvedComponents);

        // When
        Module actualModule = step.run(inputModule);

        // Then
        assertThat(actualModule.state()).isEqualTo(RESOLVED);
    }

    @Test
    void shouldNotStopModuleWhenStateIsStopped() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);
        inputModule.resolve(resolvedComponents);
        inputModule.stop(asList(flow1, flow2));

        // When
        Module actualModule = step.run(inputModule);

        // Then
        assertThat(actualModule.state()).isEqualTo(STOPPED);
    }

    @Test
    void shouldStopAllFlowsWhenStateIsStartedAndTransitionToResolved() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);
        inputModule.resolve(resolvedComponents);
        inputModule.stop(asList(flow1, flow2));
        inputModule.start(asList(flow1, flow2));

        // When
        Module actualModule = step.run(inputModule);

        // Then
        assertThat(actualModule.state()).isEqualTo(RESOLVED);

        verify(flow1).getFlowId();
        verify(flow1).getFlowTitle();
        verify(flow1).stopIfStarted();
        verify(flow1).releaseReferences(bundle);

        verify(flow2).getFlowId();
        verify(flow2).getFlowTitle();
        verify(flow2).stopIfStarted();
        verify(flow2).releaseReferences(bundle);

        verifyNoMoreInteractions(flow1, flow2);
    }

    @Test
    void shouldTransitionToErrorStateWhenFlowIsStoppedAndExceptionThrown() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);
        inputModule.resolve(resolvedComponents);
        inputModule.stop(asList(flow1, flow2, flow3));
        inputModule.start(asList(flow1, flow2, flow3));

        doThrow(new PlatformException("Listener configuration is missing"))
                .when(flow2)
                .stopIfStarted();
        doReturn("aabbccddee")
                .when(flow2)
                .getFlowId();

        doThrow(new PlatformException("Client configuration was not provided"))
                .when(flow3)
                .stopIfStarted();
        doReturn("ffghhiillmm")
                .when(flow3)
                .getFlowId();

        // When
        Module actualModule = step.run(inputModule);

        // Then
        assertThat(actualModule.state()).isEqualTo(ERROR);

        verify(flow1).getFlowId();
        verify(flow1).getFlowTitle();
        verify(flow1).stopIfStarted();
        verify(flow1).releaseReferences(bundle);

        verify(flow2).getFlowId();
        verify(flow2).getFlowTitle();
        verify(flow2).stopIfStarted();
        verify(flow2).releaseReferences(bundle);

        verify(flow3).getFlowId();
        verify(flow3).getFlowTitle();
        verify(flow3).stopIfStarted();
        verify(flow3).releaseReferences(bundle);

        verifyNoMoreInteractions(flow1, flow2, flow3);

        Collection<Exception> errors = actualModule.errors();
        assertThat(errors).hasSize(2);

        String expectedMessage1 = "{\n" +
                "  \"moduleName\": \"StopTestModule\",\n" +
                "  \"errorMessage\": \"Listener configuration is missing\",\n" +
                "  \"moduleId\": 33,\n" +
                "  \"flowId\": \"aabbccddee\",\n" +
                "  \"errorType\": \"com.reedelk.runtime.api.exception.PlatformException\"\n" +
                "}";
        String expectedMessage2 = "{\n" +
                "  \"moduleName\": \"StopTestModule\",\n" +
                "  \"errorMessage\": \"Client configuration was not provided\",\n" +
                "  \"moduleId\": 33,\n" +
                "  \"flowId\": \"ffghhiillmm\",\n" +
                "  \"errorType\": \"com.reedelk.runtime.api.exception.PlatformException\"\n" +
                "}";

        assertThatExistExceptionWithMessage(errors, expectedMessage1);
        assertThatExistExceptionWithMessage(errors, expectedMessage2);
    }

    private void assertThatExistExceptionWithMessage(Collection<Exception> errors, String expectedMessage) {
        boolean found = errors.stream().anyMatch(e -> expectedMessage.equals(e.getMessage()));
        assertThat(found).isTrue();
    }
}
