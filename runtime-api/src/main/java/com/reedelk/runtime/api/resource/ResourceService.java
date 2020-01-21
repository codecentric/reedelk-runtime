package com.reedelk.runtime.api.resource;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

public interface ResourceService {

    ResourceFile<byte[]> find(DynamicResource resource, FlowContext flowContext, Message message) throws ResourceNotFound;

    ResourceFile<byte[]> find(DynamicResource resource, int readBufferSize, FlowContext flowContext, Message message) throws ResourceNotFound;

}
