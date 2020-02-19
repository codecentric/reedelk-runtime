package com.reedelk.admin.console;

import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.rest.api.InternalAPI;
import com.reedelk.runtime.rest.api.module.v1.ModulePUTReq;
import com.reedelk.runtime.rest.api.module.v1.ModulePUTRes;
import com.reedelk.runtime.system.api.ModuleService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent(name = "Update module")
@Component(service = ModuleUpdate.class, scope = PROTOTYPE)
public class ModuleUpdate implements ProcessorSync {

    @Reference
    private ModuleService moduleService;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        String payload = message.payload();

        String resultJson = update(payload);

        return MessageBuilder.get().withJson(resultJson).build();
    }

    private String update(String json) {

        ModulePUTReq putRequest = InternalAPI.Module.V1.PUT.Req.deserialize(json);

        long moduleId = moduleService.update(putRequest.getModuleFilePath());

        ModulePUTRes dto = new ModulePUTRes();

        dto.setModuleId(moduleId);

        return InternalAPI.Module.V1.PUT.Res.serialize(dto);
    }
}
