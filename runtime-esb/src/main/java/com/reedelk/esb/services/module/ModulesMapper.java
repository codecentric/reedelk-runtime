package com.reedelk.esb.services.module;

import com.reedelk.esb.module.Module;
import com.reedelk.esb.module.state.ModuleState;
import com.reedelk.runtime.api.commons.StackTraceUtils;
import com.reedelk.runtime.system.api.FlowDto;
import com.reedelk.runtime.system.api.ModuleDto;

import java.util.Collection;

import static com.reedelk.esb.module.state.ModuleState.*;
import static java.util.stream.Collectors.toList;

public class ModulesMapper {

    private final FlowsMapper flowsMapper = new FlowsMapper();

    public ModuleDto map(Module module) {
        ModuleState state = module.state();
        ModuleDto moduleDto = new ModuleDto();
        moduleDto.setState(state.name());
        moduleDto.setName(module.name());
        moduleDto.setModuleId(module.id());
        moduleDto.setVersion(module.version());
        moduleDto.setModuleFilePath(module.filePath());

        if (STARTED == state || STOPPED == state || RESOLVED == state) {
            moduleDto.setResolvedComponents(module.resolvedComponents());
        }
        if (STARTED == state) {
            // Flows are only available when the state is STARTED.
            Collection<FlowDto> flowDTOs = flowsMapper.map(module.flows());
            moduleDto.setFlows(flowDTOs);
        }
        if (UNRESOLVED == state) {
            moduleDto.setUnresolvedComponents(module.unresolvedComponents());
            moduleDto.setResolvedComponents(module.resolvedComponents());
        }
        if (ERROR == state) {
            moduleDto.setErrors(serializeExceptions(module.errors()));
        }
        return moduleDto;
    }

    private Collection<String> serializeExceptions(Collection<Exception> exceptions) {
        return exceptions
                .stream()
                .map(StackTraceUtils::asString)
                .collect(toList());
    }
}
