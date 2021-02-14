package de.codecentric.reedelk.runtime.rest.api.hotswap.v1;

public class HotSwapPOSTResNotFound {

    private String moduleFilePath;
    private String resourcesRootDirectory;

    public String getModuleFilePath() {
        return moduleFilePath;
    }

    public void setModuleFilePath(String moduleFilePath) {
        this.moduleFilePath = moduleFilePath;
    }

    public String getResourcesRootDirectory() {
        return resourcesRootDirectory;
    }

    public void setResourcesRootDirectory(String resourcesRootDirectory) {
        this.resourcesRootDirectory = resourcesRootDirectory;
    }
}
