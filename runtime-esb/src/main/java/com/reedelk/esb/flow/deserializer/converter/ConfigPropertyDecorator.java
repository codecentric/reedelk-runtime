package com.reedelk.esb.flow.deserializer.converter;

import com.reedelk.runtime.api.commons.ConfigurationPropertyUtils;
import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.converter.DeserializerConverter;
import com.reedelk.runtime.converter.DeserializerConverterContext;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConfigPropertyDecorator implements DeserializerConverter {

    private final DeserializerConverter delegate;
    private final ConfigurationService configurationService;

    public ConfigPropertyDecorator(ConfigurationService configurationService, DeserializerConverter delegate) {
        this.configurationService = configurationService;
        this.delegate = delegate;
    }

    @Override
    public boolean isPrimitive(Class<?> clazz) {
        return delegate.isPrimitive(clazz);
    }

    @Override
    public <T> T convert(Class<T> expectedClass, JSONObject jsonObject, String propertyName, DeserializerConverterContext context) {
        // Note that a component definition might be null for some types. For instance, the ModuleId type
        // does not require any component definition in order to be instantiated, it only requires the moduleId.
        if (jsonObject != null ) {

            Object propertyValue = jsonObject.get(propertyName);

            // If component definition value is a string and starts with $[],
            // then it is a system property.
            if (ConfigurationPropertyUtils.isConfigProperty(propertyValue)) {
                String propertyKey = ConfigurationPropertyUtils.unwrap((String) propertyValue);
                return configurationService.get(propertyKey, expectedClass);
            }
        }

        // Otherwise we use the default converter.
        return delegate.convert(expectedClass, jsonObject, propertyName, context);
    }

    @Override
    public <T> T convert(Class<T> expectedClass, JSONArray jsonArray, int index, DeserializerConverterContext context) {
        Object value = jsonArray.get(index);
        // If the array value is a string and starts with $[],
        // then it is a system property, otherwise we just use the default value.
        if (ConfigurationPropertyUtils.isConfigProperty(value)) {
            String propertyKey = ConfigurationPropertyUtils.unwrap((String) value);
            return configurationService.get(propertyKey, expectedClass);
        }

        // Otherwise we use the default converter.
        return delegate.convert(expectedClass, jsonArray, index, context);
    }
}
