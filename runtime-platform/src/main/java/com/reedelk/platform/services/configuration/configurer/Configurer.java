package com.reedelk.platform.services.configuration.configurer;

import org.osgi.service.cm.ConfigurationAdmin;

public interface Configurer {

    /*
     * Applies a configuration given the config file. Returns true
     * if and only if the configuration has been applied.
     */
    boolean apply(ConfigurationAdmin configService, ConfigFile<?> configFile);

}
