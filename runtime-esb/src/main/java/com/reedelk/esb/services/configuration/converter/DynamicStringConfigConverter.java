package com.reedelk.esb.services.configuration.converter;

import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;

public class DynamicStringConfigConverter implements ConfigConverter<DynamicString> {

    private final StringConfigConverter delegate = new StringConfigConverter();

    @Override
    public DynamicString convert(ConfigurationService configurationService, String pid, String key, DynamicString defaultValue) {
        throw new UnsupportedOperationException("Not supported for dynamic typed values");
    }

    @Override
    public DynamicString convert(ConfigurationService configurationService, String pid, String key) {
        String configValue = delegate.convert(configurationService, pid, key);
        // A config value cannot be a script, hence we don't provide the module id needed only for script functions.
        return DynamicString.from(configValue);
    }
}
