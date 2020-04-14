package com.reedelk.platform.services.configuration.converter;

import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicBigInteger;

import java.math.BigInteger;

public class DynamicBigIntegerConfigConverter implements ConfigConverter<DynamicBigInteger> {

    private final BigIntegerConfigConverter delegate = new BigIntegerConfigConverter();

    @Override
    public DynamicBigInteger convert(ConfigurationService configurationService, String pid, String key, DynamicBigInteger defaultValue) {
        throw new UnsupportedOperationException("Not supported for dynamic typed values");
    }

    @Override
    public DynamicBigInteger convert(ConfigurationService configurationService, String pid, String key) {
        BigInteger configValue = delegate.convert(configurationService, pid, key);
        // A config value cannot be a script, hence we don't provide the module id needed only for script functions.
        return DynamicBigInteger.from(configValue);
    }
}
