package de.codecentric.reedelk.platform.services.resource;

import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.resource.DynamicResource;
import de.codecentric.reedelk.runtime.api.resource.ResourceFile;
import de.codecentric.reedelk.runtime.api.resource.ResourceNotFound;
import de.codecentric.reedelk.runtime.api.resource.ResourceService;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import org.reactivestreams.Publisher;

import static de.codecentric.reedelk.platform.commons.Messages.Resource;

public class DefaultResourceService implements ResourceService {

    public static final int DEFAULT_READ_BUFFER_SIZE = 65536;

    private ScriptEngineService scriptEngineService;

    public DefaultResourceService(ScriptEngineService scriptEngineService) {
        this.scriptEngineService = scriptEngineService;
    }

    @Override
    public ResourceFile<byte[]> find(DynamicResource resource, FlowContext flowContext, Message message) throws ResourceNotFound {
        return find(resource, DEFAULT_READ_BUFFER_SIZE, flowContext, message);
    }

    @Override
    public ResourceFile<byte[]> find(DynamicResource resource, int readBufferSize, FlowContext flowContext, Message message) {
        if (resource == null) {
            String errorMessage = Resource.ERROR_RESOURCE_NOT_FOUND_NULL.format();
            throw new ResourceNotFound(errorMessage);
        }
        return scriptEngineService
                .evaluate(resource, flowContext, message)
                .map(evaluatedPath -> {
                    Publisher<byte[]> data = resource.data(evaluatedPath, readBufferSize);
                    return new DefaultResourceFile(data, evaluatedPath);
                })
                .orElseThrow(() -> {
                    String errorMessage = Resource.ERROR_RESOURCE_NOT_FOUND_WITH_VALUE.format(resource.value());
                    return new ResourceNotFound(errorMessage);
                });
    }
}
