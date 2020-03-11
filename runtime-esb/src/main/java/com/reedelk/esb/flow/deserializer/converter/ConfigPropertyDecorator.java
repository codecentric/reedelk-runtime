package com.reedelk.esb.flow.deserializer.converter;

import com.reedelk.runtime.api.commons.ConfigurationPropertyUtils;
import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.converter.DeserializerConverter;
import com.reedelk.runtime.converter.DeserializerConverterContext;
import com.reedelk.runtime.converter.RuntimeConverters;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Optional;

public class ConfigPropertyDecorator implements DeserializerConverter {

    private final DeserializerConverter delegate;
    private final ConfigurationService configurationService;

    public ConfigPropertyDecorator(ConfigurationService configurationService, DeserializerConverter delegate) {
        this.configurationService = configurationService;
        this.delegate = delegate;
    }

    @Override
    public boolean isPrimitive(String fullyQualifiedName) {
        return delegate.isPrimitive(fullyQualifiedName);
    }

    @Override
    public <T> T convert(Class<T> expectedClass, JSONObject jsonObject, String propertyName, DeserializerConverterContext context) {
        // Note that a component definition might be null for some types. For instance, the ModuleId type
        // does not require any component definition in order to be instantiated, it only requires the moduleId.
        if (jsonObject != null) {

            Object propertyValue = jsonObject.get(propertyName);

            // If component definition value is a string surrounded by ${...}, then it is a system property.
            // A config property could be expressed as ${myConfigProperty} or ${myConfigProperty:myDefaultValue}
            // to use a default value when the config property could not be found.
            if (ConfigurationPropertyUtils.isConfigProperty(propertyValue)) {
                return resolveConfigProperty(expectedClass, (String) propertyValue);
            }
        }

        // Otherwise we use the default converter.
        return delegate.convert(expectedClass, jsonObject, propertyName, context);
    }

    @Override
    public <T> T convert(Class<T> expectedClass, JSONArray jsonArray, int index, DeserializerConverterContext context) {
        Object propertyValue = jsonArray.get(index);

        // If component definition value is a string surrounded by ${...}, then it is a system property.
        // A config property could be expressed as ${myConfigProperty} or ${myConfigProperty:myDefaultValue}
        // to use a default value when the config property could not be found.
        if (ConfigurationPropertyUtils.isConfigProperty(propertyValue)) {
            return resolveConfigProperty(expectedClass, (String) propertyValue);
        }

        // Otherwise we use the default converter.
        return delegate.convert(expectedClass, jsonArray, index, context);
    }

    private <T> T resolveConfigProperty(Class<T> expectedClass, String propertyValue) {
        ConfigPropertyDefinition definition = ConfigPropertyDefinition.from(propertyValue);
        String key = definition.getConfigPropertyKey();
        Optional<String> optionalDefaultValue = definition.getDefaultValue();
        if (optionalDefaultValue.isPresent()) {
            T convertedDefaultValue = RuntimeConverters.getInstance().convert(optionalDefaultValue.get(), expectedClass);
            return configurationService.get(key, convertedDefaultValue, expectedClass);
        } else {
            return configurationService.get(key, expectedClass);
        }
    }
}
