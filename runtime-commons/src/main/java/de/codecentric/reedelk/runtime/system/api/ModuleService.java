package de.codecentric.reedelk.runtime.system.api;

public interface ModuleService {

    long install(String moduleJarPath);

    long update(String moduleJarPath);

    long installOrUpdate(String moduleJarPath);

    /**
     * Uninstall the module at the given path from the runtime.
     * @param moduleJarPath the path on the file system of the module to be uninstalled.
     * @return the id of the uninstalled module or -1 if the module did not exist on the runtime.
     */
    long uninstall(String moduleJarPath);

    ModulesDto modules();

}
