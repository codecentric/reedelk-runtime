package de.codecentric.reedelk.platform.services.configuration.converter;

import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicBigDecimal;

import java.math.BigDecimal;

public class DynamicBigDecimalConfigConverter implements ConfigConverter<DynamicBigDecimal> {

    private final BigDecimalConfigConverter delegate = new BigDecimalConfigConverter();

    @Override
    public DynamicBigDecimal convert(ConfigurationService configurationService, String pid, String key, DynamicBigDecimal defaultValue) {
        throw new UnsupportedOperationException("Not supported for dynamic typed values");
    }

    @Override
    public DynamicBigDecimal convert(ConfigurationService configurationService, String pid, String key) {
        BigDecimal configValue = delegate.convert(configurationService, pid, key);
        // A config value cannot be a script, hence we don't provide the module id needed only for script functions.
        return DynamicBigDecimal.from(configValue);
    }
}
