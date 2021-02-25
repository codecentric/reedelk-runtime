package de.codecentric.reedelk.platform.flow;

import de.codecentric.reedelk.platform.test.utils.TestComponent;
import de.codecentric.reedelk.platform.component.fork.ForkWrapper;
import de.codecentric.reedelk.platform.component.router.RouterWrapper;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.api.component.Component;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import de.codecentric.reedelk.runtime.component.Stop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReleaseReferenceConsumerTest {

    @Mock
    private Bundle bundle;
    @Mock
    private BundleContext context;
    @Mock
    private ServiceObjects<Component> serviceObjects;
    @Mock
    private ServiceReference<Component> serviceReference;

    @BeforeEach
    void setUp() {
        doReturn(serviceObjects).when(context).getServiceObjects(serviceReference);
        doReturn(context).when(bundle).getBundleContext();
        doReturn(Bundle.ACTIVE).when(bundle).getState();
    }

    @Test
    void shouldNotReleaseAnyReferenceAndClearReferencesWhenComponentIsStop() {
        // Given
        Component stop = new Stop();
        ExecutionNode executionNode = mockExecutionNodeWithComponent(stop);

        // When
        ReleaseReferenceConsumer.get(bundle).accept(executionNode);

        // Then
        verify(executionNode).clearReferences();
        verifyZeroInteractions(context);
    }

    @Test
    void shouldNotReleaseAnyReferenceAndClearReferencesWhenComponentIsFork() {
        // Given
        Component fork = new ForkWrapper();
        ExecutionNode executionNode = mockExecutionNodeWithComponent(fork);

        // When
        ReleaseReferenceConsumer.get(bundle).accept(executionNode);

        // Then
        verify(executionNode).clearReferences();
        verifyZeroInteractions(context);
    }

    @Test
    void shouldNotReleaseAnyReferenceAndClearReferencesWhenComponentIsRouter() {
        // Given
        Component router = new RouterWrapper();
        ExecutionNode executionNode = mockExecutionNodeWithComponent(router);

        // When
        ReleaseReferenceConsumer.get(bundle).accept(executionNode);

        // Then
        verify(executionNode).clearReferences();
        verifyZeroInteractions(context);
    }

    @Test
    void shouldReleaseComponentReferencesAndClearReferencesWhenIsNotESBComponent() {
        // Given
        Component testComponent = new TestComponent();
        ExecutionNode executionNode = mockExecutionNodeWithComponentAndReference(testComponent, serviceReference);

        // When
        ReleaseReferenceConsumer.get(bundle).accept(executionNode);

        // Then
        verify(executionNode).clearReferences();
        verify(context).ungetService(serviceReference);
        verify(context).getServiceObjects(serviceReference);
        verify(serviceObjects).ungetService(testComponent);
        verifyNoMoreInteractions(context, serviceObjects);
    }

    @Test
    void shouldReleaseAllComponentDependenciesImplementorsAndClearReferencesWhenIsNotESBComponent() {
        // Given
        ServiceObjects<Implementor> serviceObjects1 = mock(ServiceObjects.class);
        ServiceReference<Implementor> dependency1ServiceReference = mock(ServiceReference.class);
        doReturn(serviceObjects1)
                .when(context)
                .getServiceObjects(dependency1ServiceReference);

        ServiceObjects<Implementor> serviceObjects2 = mock(ServiceObjects.class);
        ServiceReference<Implementor> dependency2ServiceReference = mock(ServiceReference.class);
        doReturn(serviceObjects2)
                .when(context)
                .getServiceObjects(dependency2ServiceReference);

        Component testComponent = new TestComponent();
        ExecutionNode executionNode = mockExecutionNodeWithComponentAndReference(testComponent, serviceReference);

        Dependency dependency1 = new Dependency();
        ExecutionNode.ReferencePair<Implementor> referencePairDependency1 = new ExecutionNode.ReferencePair<>(dependency1, dependency1ServiceReference);
        executionNode.add(referencePairDependency1);

        Dependency dependency2 = new Dependency();
        ExecutionNode.ReferencePair<Implementor> referencePairDependency2 = new ExecutionNode.ReferencePair<>(dependency2, dependency2ServiceReference);
        executionNode.add(referencePairDependency2);

        // When
        ReleaseReferenceConsumer.get(bundle).accept(executionNode);

        verify(executionNode).clearReferences();

        // Then (component's services released)
        verify(context).ungetService(serviceReference);
        verify(context).getServiceObjects(serviceReference);
        verify(serviceObjects).ungetService(testComponent);

        // Then (dependency1's services released)
        verify(context).ungetService(dependency1ServiceReference);
        verify(context).getServiceObjects(dependency1ServiceReference);
        verify(serviceObjects1).ungetService(dependency1);

        // Then (dependency2's services released)
        verify(context).ungetService(dependency2ServiceReference);
        verify(context).getServiceObjects(dependency2ServiceReference);
        verify(serviceObjects2).ungetService(dependency2);

        verifyNoMoreInteractions(context, dependency1ServiceReference, serviceObjects1, dependency2ServiceReference, serviceObjects2);
    }

    @Test
    void shouldLogWarningWhenServiceCouldNotBeProperlyReleased() {
        // Given
        ReleaseReferenceConsumer consumer = spy(ReleaseReferenceConsumer.get(bundle));

        Component testComponent = new TestComponent();
        ExecutionNode executionNode = mockExecutionNodeWithComponentAndReference(testComponent, serviceReference);

        doReturn(true).when(context).ungetService(serviceReference);

        // When
        consumer.accept(executionNode);


        // Then
        verify(consumer).warnServiceNotReleased(serviceReference);
    }

    @Test
    void shouldNotReleaseOSGiReferencesWhenBundleStateIsNotActive() {
        // Given
        doReturn(Bundle.RESOLVED).when(bundle).getState();

        Component testComponent = new TestComponent();
        ExecutionNode executionNode = mockExecutionNodeWithComponentAndReference(testComponent, serviceReference);

        // When
        ReleaseReferenceConsumer.get(bundle).accept(executionNode);

        // Then
        verify(bundle, never()).getBundleContext();
        verify(executionNode).clearReferences();
    }

    private class Dependency implements Implementor {
    }

    private ExecutionNode mockExecutionNodeWithComponent(Component component) {
        return spy(new ExecutionNode(new ExecutionNode.ReferencePair<>(component)));
    }

    private ExecutionNode mockExecutionNodeWithComponentAndReference(Component component, ServiceReference<Component> serviceReference) {
        return spy(new ExecutionNode(new ExecutionNode.ReferencePair<>(component, serviceReference)));
    }
}
