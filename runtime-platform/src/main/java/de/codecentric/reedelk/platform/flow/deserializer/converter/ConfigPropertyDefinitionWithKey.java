package de.codecentric.reedelk.platform.flow.deserializer.converter;

public class ConfigPropertyDefinitionWithKey implements ConfigPropertyDefinition {

    private final String configPropertyKey;

    public ConfigPropertyDefinitionWithKey(String configPropertyKey) {
        this.configPropertyKey = configPropertyKey;
    }

    @Override
    public String getConfigPropertyKey() {
        return configPropertyKey;
    }
}
