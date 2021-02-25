package de.codecentric.reedelk.platform.services.module;

import de.codecentric.reedelk.runtime.commons.ModuleProperties;
import org.osgi.framework.*;

import java.util.Dictionary;

import static de.codecentric.reedelk.platform.commons.ServiceReferenceProperty.COMPONENT_NAME;
import static de.codecentric.reedelk.runtime.api.commons.Preconditions.checkArgument;
import static org.osgi.framework.BundleEvent.*;
import static org.osgi.framework.ServiceEvent.REGISTERED;
import static org.osgi.framework.ServiceEvent.UNREGISTERING;

public class EventService implements BundleListener, ServiceListener {

    private final EventListener listener;

    public EventService(EventListener listener) {
        checkArgument(listener != null, "listener must not be null");
        this.listener = listener;
    }

    @Override
    public void bundleChanged(BundleEvent bundleEvent) {
        // We just  want to notify listeners if and only if
        // the bundle event is related to an ESB Module.
        if (isNotESBModule(bundleEvent.getBundle())) {
            return;
        }

        long bundleId = bundleEvent.getBundle().getBundleId();

        if (INSTALLED == bundleEvent.getType()) {
            listener.moduleInstalled(bundleId);
        }

        if (STARTED == bundleEvent.getType()) {
            listener.moduleStarted(bundleId);
        }

        if (STOPPED == bundleEvent.getType()) {
            listener.moduleStopped(bundleId);
        }

        if (UNINSTALLED == bundleEvent.getType()) {
            listener.moduleUninstalled(bundleId);
        }
    }

    @Override
    public void serviceChanged(ServiceEvent serviceEvent) {
        // We just  want to notify listeners if and only if
        // the Service event is related to an ESB Module.
        if (isNotESBModule(serviceEvent.getServiceReference().getBundle())) {
            return;
        }

        String componentName = COMPONENT_NAME.get(serviceEvent.getServiceReference());
        if (componentName == null) return;

        if (UNREGISTERING == serviceEvent.getType()) {
            listener.componentUnregistering(componentName);
        }

        if (REGISTERED == serviceEvent.getType()) {
            listener.componentRegistered(componentName);
        }
    }

    private static boolean isNotESBModule(Bundle bundle) {
        Dictionary<String, String> bundleHeaders = bundle.getHeaders();
        String isModule = bundleHeaders.get(ModuleProperties.Bundle.INTEGRATION_MODULE_HEADER);
        return !Boolean.parseBoolean(isModule);
    }
}
