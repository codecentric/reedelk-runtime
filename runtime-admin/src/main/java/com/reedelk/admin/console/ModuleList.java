package com.reedelk.admin.console;

import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.rest.api.InternalAPI;
import com.reedelk.runtime.rest.api.module.v1.ErrorGETRes;
import com.reedelk.runtime.rest.api.module.v1.FlowGETRes;
import com.reedelk.runtime.rest.api.module.v1.ModuleGETRes;
import com.reedelk.runtime.rest.api.module.v1.ModulesGETRes;
import com.reedelk.runtime.system.api.ModuleService;
import com.reedelk.runtime.system.api.ModulesDto;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;


@ModuleComponent("List modules")
@Component(service = ModuleList.class, scope = PROTOTYPE)
public class ModuleList implements ProcessorSync {

    @Reference
    private ModuleService moduleService;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        String modulesAsJson = modules();

        return MessageBuilder.get(ModuleList.class)
                .withJson(modulesAsJson)
                .build();
    }

    private String modules() {
        ModulesDto modulesDto = moduleService.modules();

        List<ModuleGETRes> modulesDTOs = modulesDto.getModuleDtos()
                .stream().map(module -> {
                    ModuleGETRes dto = new ModuleGETRes();
                    dto.setName(module.getName());
                    dto.setState(module.getState());
                    dto.setVersion(module.getVersion());
                    dto.setModuleId(module.getModuleId());
                    dto.setModuleFilePath(module.getModuleFilePath());
                    dto.setResolvedComponents(module.getResolvedComponents());
                    dto.setUnresolvedComponents(module.getUnresolvedComponents());

                    // Map Flow
                    dto.setFlows(module.getFlows().stream().map(flowDto -> {
                        FlowGETRes flow = new FlowGETRes();
                        flow.setId(flowDto.getId());
                        flow.setTitle(flowDto.getTitle());
                        return flow;
                    }).collect(toList()));

                    // Map Error
                    dto.setErrors(module.getErrors().stream().map(errorDto -> {
                        ErrorGETRes error = new ErrorGETRes();
                        error.setStacktrace(errorDto.getStacktrace());
                        error.setMessage(errorDto.getMessage());
                        return error;
                    }).collect(toList()));
                    return dto;
                }).collect(toList());

        ModulesGETRes response = new ModulesGETRes();
        response.setModules(modulesDTOs);
        return InternalAPI.Module.V1.GET.Res.serialize(response);
    }
}
