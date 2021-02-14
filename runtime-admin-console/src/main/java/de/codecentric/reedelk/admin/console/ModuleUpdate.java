package de.codecentric.reedelk.admin.console;

import de.codecentric.reedelk.runtime.api.annotation.ModuleComponent;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.rest.api.InternalAPI;
import de.codecentric.reedelk.runtime.rest.api.module.v1.ModulePUTReq;
import de.codecentric.reedelk.runtime.rest.api.module.v1.ModulePUTRes;
import de.codecentric.reedelk.runtime.system.api.ModuleService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Update module")
@Component(service = ModuleUpdate.class, scope = PROTOTYPE)
public class ModuleUpdate implements ProcessorSync {

    @Reference
    private ModuleService moduleService;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        String payload = message.payload();

        String resultJson = update(payload);

        return MessageBuilder.get(ModuleUpdate.class)
                .withJson(resultJson)
                .build();
    }

    private String update(String json) {

        ModulePUTReq putRequest = InternalAPI.Module.V1.PUT.Req.deserialize(json);

        long moduleId = moduleService.update(putRequest.getModuleFilePath());

        ModulePUTRes dto = new ModulePUTRes();

        dto.setModuleId(moduleId);

        return InternalAPI.Module.V1.PUT.Res.serialize(dto);
    }
}
