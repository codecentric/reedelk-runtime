package de.codecentric.reedelk.platform.flow.deserializer.converter;

import de.codecentric.reedelk.platform.commons.Messages;
import de.codecentric.reedelk.platform.module.DeSerializedModule;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.resource.ResourceNotFound;
import de.codecentric.reedelk.runtime.api.script.Script;
import de.codecentric.reedelk.runtime.converter.DeserializerConverter;
import de.codecentric.reedelk.runtime.converter.DeserializerConverterContext;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Optional;

public class ScriptResolverDecorator implements DeserializerConverter {

    private final DeserializerConverter delegate;
    private final DeSerializedModule deSerializedModule;

    public ScriptResolverDecorator(DeserializerConverter delegate, DeSerializedModule deSerializedModule) {
        this.delegate = delegate;
        this.deSerializedModule = deSerializedModule;
    }

    @Override
    public boolean isPrimitive(String fullyQualifiedName) {
        return delegate.isPrimitive(fullyQualifiedName);
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
        if (StringUtils.isBlank(script.getScriptPath())) {
            throw new PlatformException(Messages.Deserializer.SCRIPT_SOURCE_EMPTY.format());
        }
        return deSerializedModule.getScripts()
                .stream()
                .filter(resourceLoader -> resourceLoader.getResourceFilePath().endsWith(script.getScriptPath()))
                .findFirst()
                .flatMap(resourceLoader -> Optional.of(new ProxyScript(script, resourceLoader.bodyAsString())))
                .orElseThrow(() -> {
                    String message = Messages.Deserializer.SCRIPT_SOURCE_NOT_FOUND.format(script.getScriptPath());
                    return new ResourceNotFound(message);
                });
    }
}
