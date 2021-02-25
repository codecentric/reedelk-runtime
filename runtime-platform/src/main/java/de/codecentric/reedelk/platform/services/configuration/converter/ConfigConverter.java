package de.codecentric.reedelk.platform.services.configuration.converter;

import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;

public interface ConfigConverter<T> {

    T convert(ConfigurationService configurationService, String pid, String key, T defaultValue);

    T convert(ConfigurationService configurationService, String pid, String key);

}
