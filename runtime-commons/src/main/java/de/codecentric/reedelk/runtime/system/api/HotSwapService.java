package de.codecentric.reedelk.runtime.system.api;

public interface HotSwapService {

    long hotSwap(String modulePath, String resourcesRootDirectory) throws ModuleNotFoundException;

}
