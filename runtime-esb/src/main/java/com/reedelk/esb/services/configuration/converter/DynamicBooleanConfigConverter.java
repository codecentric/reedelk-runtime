package com.reedelk.esb.services.configuration.converter;

import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicBoolean;

public class DynamicBooleanConfigConverter implements ConfigConverter<DynamicBoolean> {

    private final BooleanConfigConverter delegate = new BooleanConfigConverter();

    @Override
    public DynamicBoolean convert(ConfigurationService configurationService, String pid, String key, DynamicBoolean defaultValue) {
        throw new UnsupportedOperationException("Not supported for dynamic typed values");
    }

    @Override
    public DynamicBoolean convert(ConfigurationService configurationService, String pid, String key) {
        boolean configValue = delegate.convert(configurationService, pid, key);
        // A config value cannot be a script, hence we don't provide the module id needed only for script functions.
        return DynamicBoolean.from(configValue);
    }
}
