package com.reedelk.esb.services.configuration.converter;

import com.reedelk.runtime.api.configuration.ConfigurationService;

public class StringConfigConverter implements ConfigConverter<String> {

    @Override
    public String convert(ConfigurationService configurationService, String pid, String key, String defaultValue) {
        return configurationService.getStringFrom(pid, key, defaultValue);
    }

    @Override
    public String convert(ConfigurationService configurationService, String pid, String key) {
        return configurationService.getStringFrom(pid, key);
    }
}
