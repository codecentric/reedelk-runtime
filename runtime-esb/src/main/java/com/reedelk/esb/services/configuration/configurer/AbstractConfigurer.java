package com.reedelk.esb.services.configuration.configurer;

import com.reedelk.runtime.api.exception.ESBException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

abstract class AbstractConfigurer implements Configurer {

    private static final String UNKNOWN_LOCATION = "?";

    @SuppressWarnings("unchecked")
    boolean updateConfigurationForPid(String pid, ConfigurationAdmin configService, Properties properties) {
        try {
            Configuration configuration = configService.getConfiguration(pid, UNKNOWN_LOCATION);
            Dictionary<String, Object> dictionary = (Hashtable) properties;
            configuration.update(dictionary);
            return true;
        } catch (IOException e) {
            throw new ESBException(e);
        }
    }
}
