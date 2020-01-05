package com.reedelk.esb.services.resource;

import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.resource.ResourceFile;
import com.reedelk.runtime.api.resource.ResourceNotFound;
import com.reedelk.runtime.api.resource.ResourceService;
import com.reedelk.runtime.api.script.ScriptEngineService;
import org.reactivestreams.Publisher;

import static com.reedelk.esb.commons.Messages.Resource;

public class DefaultResourceService implements ResourceService {

    private ScriptEngineService scriptEngineService;

    public DefaultResourceService(ScriptEngineService scriptEngineService) {
        this.scriptEngineService = scriptEngineService;
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