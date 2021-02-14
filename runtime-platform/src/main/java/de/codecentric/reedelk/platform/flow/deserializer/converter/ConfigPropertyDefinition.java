package de.codecentric.reedelk.platform.flow.deserializer.converter;

import de.codecentric.reedelk.runtime.api.commons.ConfigurationPropertyUtils;

import java.util.Optional;

public interface ConfigPropertyDefinition {

    // Config property value could be: my.config.property or my.config.property:myDefaultValue
    static ConfigPropertyDefinition from(String configPropertyValue) {
        String propertyKeyAndMaybeDefaultValue = ConfigurationPropertyUtils.unwrap(configPropertyValue);
        String[] split = propertyKeyAndMaybeDefaultValue.split(":");
        return split.length > 1 ?
                new ConfigPropertyDefinitionWithKeyAndDefault(split[0], split[1]) :
                new ConfigPropertyDefinitionWithKey(split[0]);
    }

    String getConfigPropertyKey();

    default Optional<String> getDefaultValue() {
        return Optional.empty();
    }
}
