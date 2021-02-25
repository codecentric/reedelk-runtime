package de.codecentric.reedelk.platform.flow;

import de.codecentric.reedelk.platform.commons.ServiceReferenceProperty;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.platform.component.RuntimeComponents;
import de.codecentric.reedelk.runtime.api.component.Component;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import static org.osgi.framework.Bundle.ACTIVE;

public class ReleaseReferenceConsumer implements Consumer<ExecutionNode> {

    private static final Logger logger = LoggerFactory.getLogger(ReleaseReferenceConsumer.class);

    private final Bundle bundle;

    private ReleaseReferenceConsumer(Bundle bundle) {
        this.bundle = bundle;
    }

    public static ReleaseReferenceConsumer get(Bundle bundle) {
        return new ReleaseReferenceConsumer(bundle);
    }

    @Override
    public void accept(ExecutionNode executionNode) {

        ExecutionNode.ReferencePair<Component> componentReference = executionNode.getComponentReference();
        Component component = componentReference.getImplementor();

        if (RuntimeComponents.is(component)) {
            // An ESB runtime component (e.g Fork, Router, Flow Reference ...)
            // does not have an associated OSGi service reference and service object
            // because it is just a java class instantiated by the core runtime.
            executionNode.clearReferences();
            return;
        }

        // Bundle state Active means STARTED
        if (bundle.getState() == ACTIVE) {
            BundleContext context = bundle.getBundleContext();
            // Release component and dependent implementors's OSGi references.
            unregisterOSGiReferences(context, componentReference);
            executionNode.getDependencyReferences().forEach(node -> unregisterOSGiReferences(context, node));
        }

        executionNode.clearReferences();

    }

    private <T extends Implementor> void unregisterOSGiReferences(BundleContext context, ExecutionNode.ReferencePair<T> referencePair) {
        ServiceReference<T> serviceReference = referencePair.getServiceReference();
        ServiceObjects<T> serviceObjects = context.getServiceObjects(serviceReference);
        safeUnregisterImplementor(referencePair.getImplementor(), serviceObjects);

        boolean released = context.ungetService(serviceReference);
        if (released) warnServiceNotReleased(serviceReference);
    }

    void warnServiceNotReleased(ServiceReference<?> serviceReference) {
        logger.warn("Service Reference {} could not be released", ServiceReferenceProperty.COMPONENT_NAME.get(serviceReference));
    }

    private <T extends Implementor> void safeUnregisterImplementor(T implementor, ServiceObjects<T> serviceObjects) {
        try {
            serviceObjects.ungetService(implementor);
        } catch (Exception e) {
            logger.warn("Implementor {} could not be released", implementor.getClass().getName());
        }
    }
}
