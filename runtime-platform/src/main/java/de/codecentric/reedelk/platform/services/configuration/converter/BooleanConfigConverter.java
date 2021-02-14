package de.codecentric.reedelk.platform.services.configuration.converter;

import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;

public class BooleanConfigConverter implements ConfigConverter<Boolean> {

    @Override
    public Boolean convert(ConfigurationService configurationService, String pid, String key, Boolean defaultValue) {
        return configurationService.getBooleanFrom(pid, key, defaultValue);
    }

    @Override
    public Boolean convert(ConfigurationService configurationService, String pid, String key) {
        return configurationService.getBooleanFrom(pid, key);
    }
}
