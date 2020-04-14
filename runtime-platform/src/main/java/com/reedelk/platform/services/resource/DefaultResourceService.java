package com.reedelk.platform.services.resource;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.resource.ResourceFile;
import com.reedelk.runtime.api.resource.ResourceNotFound;
import com.reedelk.runtime.api.resource.ResourceService;
import com.reedelk.runtime.api.script.ScriptEngineService;
import org.reactivestreams.Publisher;

import static com.reedelk.platform.commons.Messages.Resource;

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
