package de.codecentric.reedelk.platform.services.configuration.converter;

import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;

public class DoubleConfigConverter implements ConfigConverter<Double> {

    @Override
    public Double convert(ConfigurationService configurationService, String pid, String key, Double defaultValue) {
        return configurationService.getDoubleFrom(pid, key, defaultValue);
    }

    @Override
    public Double convert(ConfigurationService configurationService, String pid, String key) {
        return configurationService.getDoubleFrom(pid, key);
    }
}
