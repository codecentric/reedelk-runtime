package com.reedelk.platform.flow.deserializer.converter;

import com.reedelk.platform.module.DeSerializedModule;
import com.reedelk.platform.module.Module;
import com.reedelk.platform.services.resource.ResourceLoader;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.resource.*;
import com.reedelk.runtime.converter.DeserializerConverter;
import com.reedelk.runtime.converter.DeserializerConverterContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import static com.reedelk.platform.commons.Messages.Resource.RESOURCE_NOT_FOUND;

public class ResourceResolverDecorator implements DeserializerConverter {

    private final Module module;
    private final DeserializerConverter delegate;
    private final DeSerializedModule deSerializedModule;

    public ResourceResolverDecorator(DeserializerConverter delegate, DeSerializedModule deSerializedModule, Module module) {
        this.module = module;
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

        if (result instanceof ResourceText) {
            ResourceText original = (ResourceText) result;
            return (T) loadResource(original, resourceTextMapping(original));
        }
        if (result instanceof ResourceBinary) {
            ResourceBinary original = (ResourceBinary) result;
            return (T) loadResource(original, resourceBinaryMapping(original));
        }
        if  (result instanceof DynamicResource) {
            Collection<ResourceLoader> resources = deSerializedModule.getResources();
            return (T) new ProxyDynamicResource((DynamicResource) result, resources, module);
        }

        return result;
    }

    @Override
    public <T> T convert(Class<T> expectedClass, JSONArray jsonArray, int index, DeserializerConverterContext context) {
        return delegate.convert(expectedClass, jsonArray, index, context);
    }

    private <T> T loadResource(ResourceFile<?> resource, Function<ResourceLoader,Optional<T>> mappingFunction) {
        return deSerializedModule.getResources()
                .stream()
                .filter(resourceLoader -> resourceLoader.getResourceFilePath().endsWith(resource.path()))
                .findFirst()
                .flatMap(mappingFunction)
                .orElseThrow(() -> {
                    String message = RESOURCE_NOT_FOUND.format(resource.path(), module.id(), module.name());
                    return new ResourceNotFound(message);
                });
    }

    private Function<ResourceLoader,Optional<ResourceBinary>> resourceBinaryMapping(ResourceFile<?> original) {
        return resourceLoader -> {
            Publisher<byte[]> byteArrayStream = resourceLoader.body();
            return Optional.of(new ProxyResourceBinary(original, byteArrayStream));
        };
    }

    private Function<ResourceLoader,Optional<ResourceText>> resourceTextMapping(ResourceFile<?> original) {
        return resourceLoader -> {
            Publisher<byte[]> byteArrayStream = resourceLoader.body();
            Publisher<String> stringStream = StreamUtils.FromByteArray.asStringStream(byteArrayStream);
            return Optional.of(new ProxyResourceText(original, stringStream));
        };
    }
}
