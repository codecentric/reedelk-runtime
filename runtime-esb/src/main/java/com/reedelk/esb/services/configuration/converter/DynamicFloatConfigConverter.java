package com.reedelk.esb.services.configuration.converter;

import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicFloat;

public class DynamicFloatConfigConverter implements ConfigConverter<DynamicFloat> {

    private final FloatConfigConverter delegate = new FloatConfigConverter();

    @Override
    public DynamicFloat convert(ConfigurationService configurationService, String pid, String key, DynamicFloat defaultValue) {
        throw new UnsupportedOperationException("Not supported for dynamic typed values");
    }

    @Override
    public DynamicFloat convert(ConfigurationService configurationService, String pid, String key) {
        float configValue = delegate.convert(configurationService, pid, key);
        // A config value cannot be a script, hence we don't provide the module id needed only for script functions.
        return DynamicFloat.from(configValue);
    }
}
