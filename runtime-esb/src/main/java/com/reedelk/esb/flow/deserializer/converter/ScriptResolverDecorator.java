package com.reedelk.esb.flow.deserializer.converter;

import com.reedelk.esb.module.DeSerializedModule;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.resource.ResourceNotFound;
import com.reedelk.runtime.api.script.Script;
import com.reedelk.runtime.converter.DeserializerConverter;
import com.reedelk.runtime.converter.DeserializerConverterContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Optional;

import static com.reedelk.esb.commons.Messages.Deserializer.SCRIPT_SOURCE_EMPTY;
import static com.reedelk.esb.commons.Messages.Deserializer.SCRIPT_SOURCE_NOT_FOUND;
import static com.reedelk.runtime.api.commons.StringUtils.isBlank;

public class ScriptResolverDecorator implements DeserializerConverter {

    private final DeserializerConverter delegate;
    private final DeSerializedModule deSerializedModule;

    public ScriptResolverDecorator(DeserializerConverter delegate, DeSerializedModule deSerializedModule) {
        this.delegate = delegate;
        this.deSerializedModule = deSerializedModule;
    }

    @Override
    public boolean isPrimitive(Class<?> clazz) {
        return delegate.isPrimitive(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(Class<T> expectedClass, JSONObject jsonObject, String propertyName, DeserializerConverterContext context) {
        T result = delegate.convert(expectedClass, jsonObject, propertyName, context);
        if (result instanceof Script) {
            return (T) loadScriptFromResources((Script) result);
        }
        return result;
    }

    @Override
    public <T> T convert(Class<T> expectedClass, JSONArray jsonArray, int index, DeserializerConverterContext context) {
        return delegate.convert(expectedClass, jsonArray, index, context);
    }

    private Script loadScriptFromResources(Script script) {
        if (isBlank(script.getScriptPath())) {
            throw new ESBException(SCRIPT_SOURCE_EMPTY.format());
        }
        return deSerializedModule.getScripts()
                .stream()
                .filter(resourceLoader -> resourceLoader.getResourceFilePath().endsWith(script.getScriptPath()))
                .findFirst()
                .flatMap(resourceLoader -> Optional.of(new ProxyScript(script, resourceLoader.bodyAsString())))
                .orElseThrow(() -> {
                    String message = SCRIPT_SOURCE_NOT_FOUND.format(script.getScriptPath());
                    return new ResourceNotFound(message);
                });
    }
}
