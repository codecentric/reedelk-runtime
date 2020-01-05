package com.reedelk.esb.flow.deserializer.converter;

import com.reedelk.esb.module.Module;
import com.reedelk.esb.services.resource.ResourceLoader;
import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.resource.ResourceNotFound;
import org.reactivestreams.Publisher;

import java.util.Collection;
import java.util.Optional;

import static com.reedelk.esb.commons.Messages.Resource.RESOURCE_DYNAMIC_NOT_FOUND;

public class ProxyDynamicResource extends DynamicResource {

    private final Collection<ResourceLoader> resourceLoader;
    private final Module module;

    public ProxyDynamicResource(DynamicResource original, Collection<ResourceLoader> resourceLoader, Module module) {
        super(original);
        this.module = module;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Publisher<byte[]> data(String evaluatedPath, int readBufferSize) {
        return resourceLoader.stream()
                .filter(loader -> loader.getResourceFilePath().endsWith(evaluatedPath))
                .findFirst()
                .flatMap(loader -> Optional.of(loader.body(readBufferSize)))
                .orElseThrow(() -> {
                    // The file at the given path was not found in the Module bundle.
                    String message = RESOURCE_DYNAMIC_NOT_FOUND.format(evaluatedPath, value(), module.id(), module.name());
                    return new ResourceNotFound(message);
                });
    }
}