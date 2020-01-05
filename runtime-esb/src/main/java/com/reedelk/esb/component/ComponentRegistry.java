package com.reedelk.esb.component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ComponentRegistry {

    final Set<String> registeredComponents = new HashSet<>();

    public ComponentRegistry(Collection<String> predefinedComponents) {
        this.registeredComponents.addAll(predefinedComponents);
    }

    public void registerComponent(String componentName) {
        registeredComponents.add(componentName);
    }

    public void unregisterComponent(String componentName) {
        registeredComponents.remove(componentName);
    }

    public Collection<String> unregisteredComponentsOf(Collection<String> components) {
        Collection<String> unregistered = new HashSet<>(components);
        unregistered.removeAll(registeredComponents);
        return unregistered;
    }
}
