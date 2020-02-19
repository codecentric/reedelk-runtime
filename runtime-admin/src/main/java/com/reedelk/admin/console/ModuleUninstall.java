package com.reedelk.admin.console;

import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.rest.api.InternalAPI;
import com.reedelk.runtime.rest.api.module.v1.ModuleDELETEReq;
import com.reedelk.runtime.rest.api.module.v1.ModuleDELETERes;
import com.reedelk.runtime.system.api.ModuleService;
import com.reedelk.runtime.system.api.SystemProperty;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.File;
import java.net.URI;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent(name = "Uninstall module")
@Component(service = ModuleUninstall.class, scope = PROTOTYPE)
public class ModuleUninstall implements ProcessorSync {

    @Reference
    private SystemProperty systemProperty;
    @Reference
    private ModuleService moduleService;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        String payload = message.payload();

        String resultJson = delete(payload);

        return MessageBuilder.get().withJson(resultJson).build();
    }

    private String delete(String json) {

        ModuleDELETEReq deleteRequest = InternalAPI.Module.V1.DELETE.Req.deserialize(json);

        String moduleFilePath = deleteRequest.getModuleFilePath();

        long moduleId = moduleService.uninstall(moduleFilePath);

        // If module file path belongs to system modules
        String modulesDirectory = systemProperty.modulesDirectory();
        if (moduleFilePath.contains(modulesDirectory)) {
            // If the module was deployed in the modules directory, then we need to remove the file.
            File fileToBeDeleted = new File(URI.create(moduleFilePath));
            if (fileToBeDeleted.exists()) {
                fileToBeDeleted.delete();
            }
        }

        ModuleDELETERes dto = new ModuleDELETERes();

        dto.setModuleId(moduleId);

        return InternalAPI.Module.V1.DELETE.Res.serialize(dto);
    }
}
