package com.reedelk.platform.services.configuration.converter;

import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicDouble;

public class DynamicDoubleConfigConverter implements ConfigConverter<DynamicDouble> {

    private final DoubleConfigConverter delegate = new DoubleConfigConverter();

    @Override
    public DynamicDouble convert(ConfigurationService configurationService, String pid, String key, DynamicDouble defaultValue) {
        throw new UnsupportedOperationException("Not supported for dynamic typed values");
    }

    @Override
    public DynamicDouble convert(ConfigurationService configurationService, String pid, String key) {
        double configValue = delegate.convert(configurationService, pid, key);
        // A config value cannot be a script, hence we don't provide the module id needed only for script functions.
        return DynamicDouble.from(configValue);
    }
}
