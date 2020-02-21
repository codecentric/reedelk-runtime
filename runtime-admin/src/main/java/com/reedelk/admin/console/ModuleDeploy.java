package com.reedelk.admin.console;

import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.Part;
import com.reedelk.runtime.api.message.content.Parts;
import com.reedelk.runtime.system.api.ModuleService;
import com.reedelk.runtime.system.api.SystemProperty;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.reedelk.runtime.api.commons.Preconditions.checkArgument;
import static com.reedelk.runtime.api.commons.Preconditions.checkState;
import static java.lang.String.format;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Module Deploy")
@Component(service = ModuleDeploy.class, scope = PROTOTYPE)
public class ModuleDeploy implements ProcessorSync {

    // must match the name in the input type file in the 'deploy module form'
    private static final String UPLOADED_MODULE_PART_NAME = "moduleFilePath";
    private static final String ATTRIBUTE_FILE_NAME = "filename";

    @Reference
    private SystemProperty systemProperty;
    @Reference
    private ModuleService moduleService;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        Parts parts = message.payload();

        checkState(parts.containsKey(UPLOADED_MODULE_PART_NAME), "Expected form upload part missing");

        Part part = parts.get(UPLOADED_MODULE_PART_NAME);

        checkState(part.getAttributes().containsKey(ATTRIBUTE_FILE_NAME),
                "Attribute file name missing");

        // Windows Explorer sends the file name including the path.
        // Therefore we must extract just the name of the deployed module.
        String jarFileNameMaybeWithPath = part.getAttributes().get(ATTRIBUTE_FILE_NAME);

        String jarFileName = new File(jarFileNameMaybeWithPath).getName();

        checkArgument(StringUtils.isNotBlank(jarFileName), "Please specify a not empty Module file path.");

        // We upload the file into the ESB 'modules' directory.
        String uploadDirectory = systemProperty.modulesDirectory();

        Path uploadFinalFileName = Paths.get(uploadDirectory, jarFileName);

        byte[] jarArchiveBytes = (byte[]) part.getContent().data();

        ByteArrayUtils.writeTo(uploadFinalFileName.toString(), jarArchiveBytes);

        String pathAsUri;
        try {

            pathAsUri = uploadFinalFileName.toUri().toURL().toString();

        } catch (MalformedURLException e) {
            throw new ESBException(format("Could not build URL from file name '%s'", uploadFinalFileName), e);
        }

        moduleService.installOrUpdate(pathAsUri);

        return MessageBuilder.get().empty().build();
    }
}
