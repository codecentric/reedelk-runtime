package de.codecentric.reedelk.platform.services.configuration;

import de.codecentric.reedelk.runtime.system.api.SystemProperty;
import org.osgi.framework.BundleContext;

public class DefaultSystemPropertyService implements SystemProperty {

    private final String configDirectory;
    private final String homeDirectory;
    private final String modulesDirectory;
    private final String runtimeVersion;
    private final String runtimeQualifier;

    public DefaultSystemPropertyService(BundleContext context) {
        // NAME_CONVENTION
        configDirectory = context.getProperty("de.codecentric.reedelk.system.api.configuration.config.directory");
        // NAME_CONVENTION
        homeDirectory = context.getProperty("de.codecentric.reedelk.system.api.configuration.home.directory");
        // NAME_CONVENTION
        modulesDirectory = context.getProperty("de.codecentric.reedelk.system.api.configuration.modules.directory");
        // NAME_CONVENTION
        runtimeVersion = context.getProperty("de.codecentric.reedelk.system.api.configuration.runtime.version");
        // NAME_CONVENTION
        runtimeQualifier = context.getProperty("de.codecentric.reedelk.system.api.configuration.runtime.qualifier");
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

    @Override
    public String qualifier() {
        return runtimeQualifier;
    }
}
