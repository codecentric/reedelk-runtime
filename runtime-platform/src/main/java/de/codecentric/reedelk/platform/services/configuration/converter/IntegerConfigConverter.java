package de.codecentric.reedelk.platform.services.configuration.converter;

import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;

public class IntegerConfigConverter implements ConfigConverter<Integer> {

    @Override
    public Integer convert(ConfigurationService configurationService, String pid, String key, Integer defaultValue) {
        return configurationService.getIntFrom(pid, key, defaultValue);
    }

    @Override
    public Integer convert(ConfigurationService configurationService, String pid, String key) {
        return configurationService.getIntFrom(pid, key);
    }
}
