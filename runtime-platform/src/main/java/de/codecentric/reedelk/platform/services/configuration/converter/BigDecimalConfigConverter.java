package de.codecentric.reedelk.platform.services.configuration.converter;

import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;

import java.math.BigDecimal;

public class BigDecimalConfigConverter implements ConfigConverter<BigDecimal> {

    @Override
    public BigDecimal convert(ConfigurationService configurationService, String pid, String key, BigDecimal defaultValue) {
        return configurationService.getBigDecimalFrom(pid, key, defaultValue);
    }

    @Override
    public BigDecimal convert(ConfigurationService configurationService, String pid, String key) {
        return configurationService.getBigDecimalFrom(pid, key);
    }
}
