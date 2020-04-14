package com.reedelk.platform.services.configuration.configurer;

import org.osgi.service.cm.ConfigurationAdmin;

import java.util.Properties;

public class LogbackConfigurer extends AbstractConfigurer {

    private static final String LOGBACK_CONFIG_FILE_NAME = "logback.xml";

    private static final String PAX_LOGGING_CONFIG_PID = "org.ops4j.pax.logging";
    private static final String LOGBACK_CONFIG_FILE_PROPERTY_NAME = "org.ops4j.pax.logging.logback.config.file";

    @Override
    public boolean apply(ConfigurationAdmin configService, ConfigFile configFile) {
        if (matches(configFile)) {
            XmlConfigFile logback = (XmlConfigFile) configFile;
            Properties properties = new Properties();
            properties.put(LOGBACK_CONFIG_FILE_PROPERTY_NAME, logback.getFilePath());
            return updateConfigurationForPid(PAX_LOGGING_CONFIG_PID, configService, properties);
        } else {
            return false;
        }

    }

    private boolean matches(ConfigFile configFile) {
        return LOGBACK_CONFIG_FILE_NAME.equalsIgnoreCase(configFile.getFileName());
    }

}
