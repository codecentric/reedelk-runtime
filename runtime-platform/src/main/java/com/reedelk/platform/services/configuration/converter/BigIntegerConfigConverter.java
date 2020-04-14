package com.reedelk.platform.services.configuration.converter;

import com.reedelk.runtime.api.configuration.ConfigurationService;

import java.math.BigInteger;

public class BigIntegerConfigConverter implements ConfigConverter<BigInteger> {

    @Override
    public BigInteger convert(ConfigurationService configurationService, String pid, String key, BigInteger defaultValue) {
        return configurationService.getBigIntegerFrom(pid, key, defaultValue);
    }

    @Override
    public BigInteger convert(ConfigurationService configurationService, String pid, String key) {
        return configurationService.getBigIntegerFrom(pid, key);
    }
}
