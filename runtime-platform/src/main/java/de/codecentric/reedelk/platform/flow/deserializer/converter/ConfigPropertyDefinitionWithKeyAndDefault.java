package de.codecentric.reedelk.platform.flow.deserializer.converter;

import java.util.Optional;

public class ConfigPropertyDefinitionWithKeyAndDefault extends ConfigPropertyDefinitionWithKey {

    private final String defaultValue;

    public ConfigPropertyDefinitionWithKeyAndDefault(String configPropertyKey, String defaultValue) {
        super(configPropertyKey);
        this.defaultValue = defaultValue;
    }

    @Override
    public Optional<String> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }
}
