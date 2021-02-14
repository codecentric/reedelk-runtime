package de.codecentric.reedelk.runtime.api.resource;

import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

public interface ResourceService {

    ResourceFile<byte[]> find(DynamicResource resource, FlowContext flowContext, Message message) throws ResourceNotFound;

    ResourceFile<byte[]> find(DynamicResource resource, int readBufferSize, FlowContext flowContext, Message message) throws ResourceNotFound;

}
