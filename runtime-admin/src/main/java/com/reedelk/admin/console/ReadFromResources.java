package com.reedelk.admin.console;

import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.commons.FileUtils;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.exception.PlatformException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.resource.ResourceFile;
import com.reedelk.runtime.api.resource.ResourceNotFound;
import com.reedelk.runtime.api.resource.ResourceService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.reactivestreams.Publisher;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Read From Resources")
@Component(service = ReadFromResources.class, scope = PROTOTYPE)
public class ReadFromResources implements ProcessorSync {

    public static final int DEFAULT_READ_BUFFER_SIZE = 65536;

    @Reference
    private ResourceService resourceService;

    @Property("Resource file")
    private DynamicResource resourceFile;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        try {

            ResourceFile<byte[]> resourceFile = resourceService.find(this.resourceFile, DEFAULT_READ_BUFFER_SIZE, flowContext, message);

            String resourceFilePath = resourceFile.path();

            String pageFileExtension = FileUtils.getExtension(resourceFilePath);

            MimeType actualMimeType = MimeType.fromFileExtension(pageFileExtension);

            Publisher<byte[]> dataStream = resourceFile.data();

            return MessageBuilder.get().withBinary(dataStream, actualMimeType).build();

        } catch (ResourceNotFound resourceNotFound) {
            throw new PlatformException(resourceNotFound);
        }
    }

    public void setResourceFile(DynamicResource resourceFile) {
        this.resourceFile = resourceFile;
    }
}
