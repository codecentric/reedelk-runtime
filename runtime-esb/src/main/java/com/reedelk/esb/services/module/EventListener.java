package com.reedelk.esb.services.module;

public interface EventListener {

    void moduleInstalled(long moduleId);

    void moduleStarted(long moduleId);

    void moduleStopping(long moduleId);

    void moduleStopped(long moduleId);

    void moduleUninstalled(long moduleId);

    void componentRegistered(String componentName);

    void componentUnregistering(String componentName);
}
