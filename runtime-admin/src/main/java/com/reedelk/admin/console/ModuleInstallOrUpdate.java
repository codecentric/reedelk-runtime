package com.reedelk.admin.console;

import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.rest.api.InternalAPI;
import com.reedelk.runtime.rest.api.module.v1.ModulePOSTReq;
import com.reedelk.runtime.rest.api.module.v1.ModulePOSTRes;
import com.reedelk.runtime.system.api.ModuleService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent(name = "Install or update module")
@Component(service = ModuleInstallOrUpdate.class, scope = PROTOTYPE)
public class ModuleInstallOrUpdate implements ProcessorSync {

    @Reference
    private ModuleService moduleService;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        String payload = message.payload();

        String resultJson = installOrUpdate(payload);

        return MessageBuilder.get().withJson(resultJson).build();
    }

    private String installOrUpdate(String json) {

        ModulePOSTReq postRequest = InternalAPI.Module.V1.POST.Req.deserialize(json);

        long moduleId = moduleService.installOrUpdate(postRequest.getModuleFilePath());

        ModulePOSTRes dto = new ModulePOSTRes();

        dto.setModuleId(moduleId);

        return InternalAPI.Module.V1.POST.Res.serialize(dto);
    }
}
