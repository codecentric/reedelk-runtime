package com.reedelk.platform.flow.deserializer.converter;

import com.reedelk.runtime.converter.DeserializerConverter;
import com.reedelk.runtime.converter.DeserializerConverterContext;
import org.json.JSONArray;
import org.json.JSONObject;

public class DeserializerConverterContextDecorator implements DeserializerConverter {

    private final DeserializerConverter delegate;
    private final DeserializerConverterContext context;

    public DeserializerConverterContextDecorator(DeserializerConverter delegate, long moduleId) {
        this.delegate = delegate;
        this.context = new DeserializerConverterContext(moduleId);
    }

    @Override
    public boolean isPrimitive(String fullyQualifiedName) {
        return delegate.isPrimitive(fullyQualifiedName);
    }

    @Override
    public <T> T convert(Class<T> expectedClass, JSONObject jsonObject, String propertyName) {
        return convert(expectedClass, jsonObject, propertyName, context);
    }

    @Override
    public <T> T convert(Class<T> expectedClass, JSONArray jsonArray, int index) {
        return convert(expectedClass, jsonArray, index, context);
    }

    @Override
    public <T> T convert(Class<T> expectedClass, JSONObject jsonObject, String propertyName, DeserializerConverterContext context) {
        return delegate.convert(expectedClass, jsonObject, propertyName, context);
    }

    @Override
    public <T> T convert(Class<T> expectedClass, JSONArray jsonArray, int index, DeserializerConverterContext context) {
        return delegate.convert(expectedClass, jsonArray, index, context);
    }
}
