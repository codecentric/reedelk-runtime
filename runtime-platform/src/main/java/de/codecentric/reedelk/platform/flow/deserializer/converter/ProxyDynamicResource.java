package de.codecentric.reedelk.platform.flow.deserializer.converter;

import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.platform.services.resource.ResourceLoader;
import de.codecentric.reedelk.runtime.api.resource.DynamicResource;
import de.codecentric.reedelk.runtime.api.resource.ResourceNotFound;
import org.reactivestreams.Publisher;

import java.util.Collection;
import java.util.Optional;

import static de.codecentric.reedelk.platform.commons.Messages.Resource.RESOURCE_DYNAMIC_NOT_FOUND;

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
