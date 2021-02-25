package de.codecentric.reedelk.admin.console;

import de.codecentric.reedelk.runtime.api.annotation.ModuleComponent;
import de.codecentric.reedelk.runtime.api.annotation.Property;
import de.codecentric.reedelk.runtime.api.commons.FileUtils;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.resource.DynamicResource;
import de.codecentric.reedelk.runtime.api.resource.ResourceFile;
import de.codecentric.reedelk.runtime.api.resource.ResourceNotFound;
import de.codecentric.reedelk.runtime.api.resource.ResourceService;
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

            MimeType actualMimeType =
                    MimeType.fromFileExtension(pageFileExtension, MimeType.APPLICATION_BINARY);

            Publisher<byte[]> dataStream = resourceFile.data();

            return MessageBuilder.get(ReadFromResources.class)
                    .withBinary(dataStream, actualMimeType)
                    .build();

        } catch (ResourceNotFound resourceNotFound) {
            throw new PlatformException(resourceNotFound);
        }
    }

    public void setResourceFile(DynamicResource resourceFile) {
        this.resourceFile = resourceFile;
    }
}
