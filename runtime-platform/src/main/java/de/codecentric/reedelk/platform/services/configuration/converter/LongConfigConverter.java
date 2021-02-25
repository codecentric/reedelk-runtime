package de.codecentric.reedelk.platform.services.configuration.converter;


import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;

public class LongConfigConverter implements ConfigConverter<Long> {

    @Override
    public Long convert(ConfigurationService configurationService, String pid, String key, Long defaultValue) {
        return configurationService.getLongFrom(pid, key, defaultValue);
    }

    @Override
    public Long convert(ConfigurationService configurationService, String pid, String key) {
        return configurationService.getLongFrom(pid, key);
    }
}
