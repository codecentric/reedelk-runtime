package com.reedelk.esb.module;

import com.reedelk.esb.flow.Flow;
import com.reedelk.esb.module.state.ModuleState;
import com.reedelk.runtime.api.exception.PlatformException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;

import static com.reedelk.esb.module.state.ModuleState.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ModuleTest {

    private final long TEST_MODULE_ID = 12;
    private final String TEST_MODULE_NAME = "ModuleNameTest";
    private final String TEST_VERSION = "0.9.0";
    private final String TEST_LOCATION = "file://location/test";

    private final Collection<String> unresolvedComponents = asList("com.reedelk.esb.Unresolved1", "com.reedelk.esb.Unresolved2");
    private final Collection<String> resolvedComponents = asList("com.reedelk.esb.Resolved1", "com.reedelk.esb.Resolved2");

    @Mock
    private Flow flow;
    @Mock
    private ModuleDeserializer deserializer;
    private Module module;

    @BeforeEach
    void setUp() {
        module = Module.builder()
                .version(TEST_VERSION)
                .name(TEST_MODULE_NAME)
                .moduleId(TEST_MODULE_ID)
                .deserializer(deserializer)
                .moduleFilePath(TEST_LOCATION)
                .build();
    }

    @Test
    void shouldCorrectlyReturnModuleId() {
        // When
        long moduleId = module.id();

        // Then
        assertThat(moduleId).isEqualTo(TEST_MODULE_ID);
    }

    @Test
    void shouldCorrectlyReturnName() {
        // When
        String name = module.name();

        // Then
        assertThat(name).isEqualTo(TEST_MODULE_NAME);
    }

    @Nested
    @DisplayName("Installed State tests")
    class Installed {

        @Test
        void shouldInitialStateBeInstalled() {
            // When
            ModuleState state = module.state();

            // Then
            assertThat(state).isEqualTo(INSTALLED);
        }

        // Installed -> Error | Unresolved
        @Test
        void shouldInstalledTransitionToErrorState() {
            // When
            module.error(new Exception());

            // Then
            assertStateIs(ERROR);
        }

        @Test
        void shouldInstalledTransitionToUnresolvedState() {
            // When
            module.unresolve(unresolvedComponents, resolvedComponents);

            // Then
            assertStateIs(UNRESOLVED);
        }

        @Test
        void shouldThrowExceptionWhenTransitionToResolved() {
            // Expect
            assertThrows(IllegalStateException.class, () -> {
                // When
                module.resolve(resolvedComponents);
            });
        }
    }

    @Nested
    @DisplayName("Unresolved state tests")
    class Unresolved {

        // Unresolved -> Resolved | Error | Unresolved | Installed
        @Test
        void shouldUnresolvedTransitionToResolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);

            // When
            module.resolve(resolvedComponents);

            // Then
            assertStateIs(RESOLVED);
        }

        @Test
        void shouldThrowExceptionWhenTransitionToError() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);

            // Expect
            assertThrows(IllegalStateException.class, () -> {
                // When
                module.error(new Exception());
            });
        }

        @Test
        void shouldUnresolvedTransitionToUnresolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);

            // When
            module.unresolve(unresolvedComponents, resolvedComponents);

            // Then
            assertStateIs(UNRESOLVED);
        }

        @Test
        void shouldUnresolvedThrowExceptionWhenTransitionToStart() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);

            // Expect
            assertThrows(IllegalStateException.class, () -> module.start(emptyList()));
        }

        @Test
        void shouldUnresolvedTransitionToInstalled() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);

            // When
            module.installed();

            // Then
            assertStateIs(INSTALLED);
        }
    }

    @Nested
    @DisplayName("Resolved state tests")
    class Resolved {

        // Resolved -> Unresolved | Stopped | Error | Installed
        @Test
        void shouldResolvedTransitionToUnresolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);

            // When
            module.unresolve(unresolvedComponents, resolvedComponents);

            // Then
            assertStateIs(UNRESOLVED);
        }

        @Test
        void shouldResolvedTransitionToStopped() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);

            // When
            module.stop(singletonList(flow));

            // Then
            assertStateIs(STOPPED);
        }

        @Test
        void shouldResolvedTransitionToError() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);

            // When
            module.error(singletonList(new Exception()));

            // Then
            assertStateIs(ERROR);
        }

        @Test
        void shouldResolvedThrowExceptionWhenTransitionToStart() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);

            // Expect
            // Expect
            assertThrows(IllegalStateException.class, () -> module.start(emptyList()));
        }

        @Test
        void shouldResolvedTransitionToInstalled() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);

            // When
            module.installed();

            // Then
            assertStateIs(INSTALLED);
        }
    }

    @Nested
    @DisplayName("Stopped state tests")
    class Stopped {

        // Stopped -> Started | Resolved | Unresolved | Error | Installed
        @Test
        void shouldStoppedTransitionToStarted() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));

            // When
            module.start(singletonList(flow));

            // Then
            assertStateIs(STARTED);
        }

        @Test
        void shouldStoppedTransitionToUnresolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));

            // When
            module.unresolve(unresolvedComponents, resolvedComponents);

            // Then
            assertStateIs(UNRESOLVED);
        }

        @Test
        void shouldStoppedTransitionToError() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));

            // When
            module.error(new Exception());

            // Then
            assertStateIs(ERROR);
        }

        @Test
        void shouldStoppedTransitionToResolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));

            // When
            module.resolve(resolvedComponents);

            // Then
            assertStateIs(RESOLVED);
        }

        @Test
        void shouldStoppedTransitionToInstalled() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));

            // When
            module.installed();

            // Then
            assertStateIs(INSTALLED);
        }
    }


    @Nested
    @DisplayName("Started state tests")
    class Started {

        // Started -> Stopped
        @Test
        void shouldStartedTransitionToStopped() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));
            module.start(singletonList(flow));

            // When
            module.stop(singletonList(flow));

            // Then
            assertStateIs(STOPPED);
        }

    }

    @Nested
    @DisplayName("Error state tests")
    class Error {

        // Error -> Unresolved | Installed

        @Test
        void shouldErrorTransitionToUnresolved() {
            // Given
            module.error(new PlatformException("Deserialization error"));

            // When
            module.unresolve(unresolvedComponents, Collections.emptyList());

            // Then
            assertStateIs(UNRESOLVED);
        }

        @Test
        void shouldErrorTransitionToInstalled() {
            // Given
            module.error(new PlatformException("Deserialization error"));

            // When
            module.installed();

            // Then
            assertStateIs(INSTALLED);
        }
    }

    @Nested
    @DisplayName("flows() method tests while in different module states")
    class GetFlows {

        @Test
        void shouldGetFlowsThrowExceptionWhenStateIsInstalled() {
            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.flows();
            });
        }

        @Test
        void shouldGetFlowsThrowExceptionWhenStateIsUnresolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);

            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.flows();
            });
        }

        @Test
        void shouldGetFlowsThrowExceptionWhenStateIsResolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);

            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.flows();
            });
        }

        @Test
        void shouldGetFlowsThrowExceptionWhenStateIsError() {
            // Given
            module.error(new Exception());

            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.flows();
            });
        }

        @Test
        void shoudlGetFlowsReturnCorrectlyWhenStateIsStopped() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));

            // When
            Collection<Flow> flows = module.flows();

            // Then
            assertThat(flows).hasSize(1);
            Flow next = flows.iterator().next();
            assertThat(next).isEqualTo(flow);
        }

        @Test
        void shoudlGetFlowsReturnCorrectlyWhenStateIsStarted() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));
            module.start(singletonList(flow));

            // When
            Collection<Flow> flows = module.flows();

            // Then
            assertThat(flows).hasSize(1);
            Flow next = flows.iterator().next();
            assertThat(next).isEqualTo(flow);
        }

    }


    @Nested
    @DisplayName("resolvedComponents() method tests while in different module states")
    class GetResolvedComponents {

        @Test
        void shouldGetResolvedComponentsThrowExceptionWhenStateIsInstalled() {
            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.resolvedComponents();
            });
        }

        @Test
        void shouldGetResolvedComponentsReturnCorrectlyWhenStateIsUnresolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);

            // When
            Collection<String> resolvedComponents = module.resolvedComponents();

            // Then
            assertThat(resolvedComponents).isEqualTo(ModuleTest.this.resolvedComponents);
        }

        @Test
        void shouldGetResolvedComponentsReturnCorrectlyWhenStateIsResolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);

            // When
            Collection<String> resolvedComponents = module.resolvedComponents();

            // Then
            assertThat(resolvedComponents).isEqualTo(ModuleTest.this.resolvedComponents);
        }

        @Test
        void shouldGetResolvedComponentsReturnCorrectlyWhenStateIsStopped() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));

            // When
            Collection<String> resolvedComponents = module.resolvedComponents();

            // Then
            assertThat(resolvedComponents).isEqualTo(ModuleTest.this.resolvedComponents);
        }

        @Test
        void shouldGetResolvedComponentsReturnCorrectlyWhenStateIsStarted() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));
            module.start(singletonList(flow));

            // When
            Collection<String> resolvedComponents = module.resolvedComponents();

            // Then
            assertThat(resolvedComponents).isEqualTo(ModuleTest.this.resolvedComponents);
        }

        @Test
        void shouldGetResolvedComponentsReturnCorrectlyWhenStateIsErrorFromInstalled() {
            // Given
            module.error(new Exception());

            // When
            Collection<String> resolvedComponents = module.resolvedComponents();

            // Then
            assertThat(resolvedComponents).isEmpty();
        }

        @Test
        void shouldGetResolvedComponentsReturnCorrectlyWhenStateIsErrorFromStopped() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));
            module.error(new Exception());

            // When
            Collection<String> resolvedComponents = module.resolvedComponents();

            // Then
            assertThat(resolvedComponents).isEqualTo(ModuleTest.this.resolvedComponents);
        }

    }

    @Nested
    @DisplayName("unresolvedComponents() method tests while in different module states")
    class GetUnresolvedComponents {

        @Test
        void shouldGetUnresolvedComponentsThrowExceptionWhenStateIsInstalled() {
            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.unresolvedComponents();
            });
        }

        @Test
        void shouldGetUnresolvedComponentsReturnCorrectlyWhenStateIsUnresolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);

            // When
            Collection<String> unresolvedComponents = module.unresolvedComponents();

            // Then
            assertThat(unresolvedComponents).isEqualTo(ModuleTest.this.unresolvedComponents);
        }

        @Test
        void shouldGetUnresolvedComponentsThrowExceptionWhenStateIsResolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);

            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.unresolvedComponents();
            });
        }

        @Test
        void shouldGetUnresolvedComponentsThrowExceptionWhenStateIsStopped() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));

            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.unresolvedComponents();
            });
        }

        @Test
        void shouldGetUnresolvedComponentsThrowExceptionWhenStateIsStarted() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));
            module.start(singletonList(flow));

            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.unresolvedComponents();
            });
        }

        @Test
        void shouldGetUnresolvedComponentsThrowExceptionWhenStateIsError() {
            // Given
            module.error(new Exception());

            // Expect
            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.unresolvedComponents();
            });
        }
    }

    @Nested
    @DisplayName("errors() method tests while in different module states")
    class GetErrors {

        @Test
        void shouldGetErrorsReturnCorrectlyWhenStateIsError() {
            // Given
            module.error(asList(new Exception("Exception1"), new Exception("Exception2")));

            // Expect
            Collection<Exception> errors = module.errors();

            // When
            assertThat(errors).hasSize(2);
        }

        @Test
        void shouldGetErrorsThrowExceptionWhenStateIsInstalled() {
            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.errors();
            });
        }

        @Test
        void shouldGetErrorsThrowExceptionWhenStateIsUnresolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);

            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.errors();
            });
        }

        @Test
        void shouldGetErrorsThrowExceptionWhenStateIsResolved() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);

            // Expect
            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.errors();
            });
        }

        @Test
        void shouldGetErrorsThrowExceptionWhenStateIsStopped() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));

            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.errors();
            });
        }

        @Test
        void shouldGetErrorsThrowExceptionWhenStateIsStarted() {
            // Given
            module.unresolve(unresolvedComponents, resolvedComponents);
            module.resolve(resolvedComponents);
            module.stop(singletonList(flow));
            module.start(singletonList(flow));

            // Expect
            assertThrows(UnsupportedOperationException.class, () -> {
                // When
                module.errors();
            });
        }
    }

    private void assertStateIs(ModuleState expectedState) {
        ModuleState state = module.state();
        assertThat(state).isEqualTo(expectedState);
    }
}
