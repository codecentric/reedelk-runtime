package com.reedelk.platform.services.configuration.converter;

import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicLong;

public class DynamicLongConfigConverter implements ConfigConverter<DynamicLong> {

    private final LongConfigConverter delegate = new LongConfigConverter();

    @Override
    public DynamicLong convert(ConfigurationService configurationService, String pid, String key, DynamicLong defaultValue) {
        throw new UnsupportedOperationException("Not supported for dynamic typed values");
    }

    @Override
    public DynamicLong convert(ConfigurationService configurationService, String pid, String key) {
        long configValue = delegate.convert(configurationService, pid, key);
        // A config value cannot be a script, hence we don't provide the module id needed only for script functions.
        return DynamicLong.from(configValue);
    }
}
