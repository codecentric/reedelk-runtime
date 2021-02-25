package de.codecentric.reedelk.platform.commons;

import org.osgi.framework.ServiceReference;

public enum ServiceReferenceProperty {

    COMPONENT_NAME("component.name");

    private String name;

    ServiceReferenceProperty(String name) {
        this.name = name;
    }

    public String get(ServiceReference<?> serviceReference) {
        return (String) serviceReference.getProperty(name);
    }

}
