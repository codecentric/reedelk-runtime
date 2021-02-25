package de.codecentric.reedelk.platform.flow.deserializer.converter;

import de.codecentric.reedelk.runtime.converter.DeserializerConverter;
import de.codecentric.reedelk.runtime.converter.DeserializerConverterContext;
import de.codecentric.reedelk.runtime.system.api.SystemProperty;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Replaces component properties containing '${RUNTIME_CONFIG}' with the actual runtime
 * values for that system property.
 */
public class SystemConfigPropertyReplacerDecorator implements DeserializerConverter {

    private final DeserializerConverter delegate;
    private final SystemProperty systemPropertyService;

    public SystemConfigPropertyReplacerDecorator(SystemProperty systemPropertyService, DeserializerConverter delegate) {
        this.systemPropertyService = systemPropertyService;
        this.delegate = delegate;
    }

    @Override
    public boolean isPrimitive(String fullyQualifiedName) {
        return delegate.isPrimitive(fullyQualifiedName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(Class<T> expectedClass, JSONObject jsonObject, String propertyName, DeserializerConverterContext context) {
        // Note that a component definition might be null for some types. For instance, the ModuleId type
        // does not require any component definition in order to be instantiated, it only requires the moduleId.
        if (jsonObject != null) {

            Object propertyValue = jsonObject.get(propertyName);

            // If component definition value contains ${RUNTIME_CONFIG} it means we must replace
            // its value with the runtime config path.
            if (propertyValue instanceof String) {
                String propertyValueAsString = (String) propertyValue;
                for (SystemConfigProperty property : SystemConfigProperty.values()) {
                    if (property.matches(propertyValueAsString)) {
                        return (T) property.replace(propertyValueAsString, systemPropertyService);
                    }
                }
            }
        }

        // Otherwise we use the default converter.
        return delegate.convert(expectedClass, jsonObject, propertyName, context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(Class<T> expectedClass, JSONArray jsonArray, int index, DeserializerConverterContext context) {
        Object propertyValue = jsonArray.get(index);

        // If component definition value contains ${RUNTIME_CONFIG} it means we must replace
        // its value with the runtime config path.
        if (propertyValue instanceof String) {
            String propertyValueAsString = (String) propertyValue;
            for (SystemConfigProperty property : SystemConfigProperty.values()) {
                if (property.matches(propertyValueAsString)) {
                    return (T) property.replace(propertyValueAsString, systemPropertyService);
                }
            }
        }

        // Otherwise we use the default converter.
        return delegate.convert(expectedClass, jsonArray, index, context);
    }
}
