package com.reedelk.platform.services.configuration;

import com.reedelk.runtime.system.api.SystemProperty;
import org.osgi.framework.BundleContext;

public class DefaultSystemPropertyService implements SystemProperty {

    private final String configDirectory;
    private final String homeDirectory;
    private final String modulesDirectory;
    private final String runtimeVersion;

    public DefaultSystemPropertyService(BundleContext context) {
        // NAME_CONVENTION
        configDirectory = context.getProperty("com.reedelk.system.api.configuration.config.directory");
        // NAME_CONVENTION
        homeDirectory = context.getProperty("com.reedelk.system.api.configuration.home.directory");
        // NAME_CONVENTION
        modulesDirectory = context.getProperty("com.reedelk.system.api.configuration.modules.directory");
        // NAME_CONVENTION
        runtimeVersion = context.getProperty("com.reedelk.system.api.configuration.runtime.version");
    }

    @Override
    public String configDirectory() {
        return configDirectory;
    }

    @Override
    public String modulesDirectory() {
        return modulesDirectory;
    }

    @Override
    public String homeDirectory() {
        return homeDirectory;
    }

    @Override
    public String version() {
        return runtimeVersion;
    }
}
