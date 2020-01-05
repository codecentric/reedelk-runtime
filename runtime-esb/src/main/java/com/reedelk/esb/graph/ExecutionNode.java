package com.reedelk.esb.graph;

import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.List;

import static com.reedelk.runtime.api.commons.Preconditions.checkArgument;

public class ExecutionNode {

    private ReferencePair<Component> componentReference;
    private List<ReferencePair<Implementor>> dependencyReferences = new ArrayList<>();

    public ExecutionNode(ReferencePair<Component> componentReference) {
        this.componentReference = componentReference;
    }

    public ReferencePair<Component> getComponentReference() {
        return componentReference;
    }

    public void add(ReferencePair<Implementor> dependencyReference) {
        this.dependencyReferences.add(dependencyReference);
    }

    public Component getComponent() {
        return componentReference.implementor;
    }

    public List<ReferencePair<Implementor>> getDependencyReferences() {
        return dependencyReferences;
    }

    public void clearReferences() {
        // Dispose the component reference
        dispose(componentReference);

        // Dispose the dependencies
        dependencyReferences.forEach(this::dispose);
        dependencyReferences.clear();

        // we help the gc to faster identify which objects can be garbage collected.
        componentReference = null;
        dependencyReferences = null;
    }

    public boolean isUsingComponent(final String targetComponentName) {
        String componentName = componentNameOf(componentReference);
        if (componentName.equals(targetComponentName)) return true;

        return dependencyReferences
                .stream()
                .anyMatch(referencePair -> componentNameOf(referencePair)
                        .equals(targetComponentName));
    }

    private String componentNameOf(ReferencePair<? extends Implementor> referencePair) {
        return referencePair.getImplementor().getClass().getName();
    }

    public void onInitializeEvent() {
        componentReference.implementor.initialize();
        dependencyReferences.forEach(implementorReferencePair ->
                implementorReferencePair.implementor.initialize());
    }

    private void dispose(ReferencePair<? extends Implementor> componentReference) {
        if (componentReference.implementor != null) {
            // We must  call dispose() before clearing Implementor's references.
            componentReference.implementor.dispose();
        }
        componentReference.implementor = null;
        componentReference.serviceReference = null;
    }

    public static class ReferencePair<T extends Implementor> {

        private T implementor;
        private ServiceReference<T> serviceReference;

        public ReferencePair(T implementor) {
            checkArgument(implementor != null, "implementor");
            this.implementor = implementor;
            this.serviceReference = null;
        }

        public ReferencePair(T implementor, ServiceReference<T> serviceReference) {
            checkArgument(implementor != null, "implementor");
            checkArgument(serviceReference != null, "service reference");
            this.implementor = implementor;
            this.serviceReference = serviceReference;
        }

        public T getImplementor() {
            return implementor;
        }

        public ServiceReference<T> getServiceReference() {
            return serviceReference;
        }
    }
}