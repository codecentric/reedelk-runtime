package com.reedelk.platform.services.module;

import com.reedelk.platform.module.Module;
import com.reedelk.platform.module.state.ModuleState;
import com.reedelk.runtime.system.api.ErrorDto;
import com.reedelk.runtime.system.api.FlowDto;
import com.reedelk.runtime.system.api.ModuleDto;

import java.util.Collection;

import static com.reedelk.platform.module.state.ModuleState.*;

public class ModulesMapper {

    private final FlowsMapper flowsMapper = new FlowsMapper();
    private final ErrorsMapper errorsMapper = new ErrorsMapper();

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
            Collection<FlowDto> flows = flowsMapper.map(module.flows());
            moduleDto.setFlows(flows);
        }
        if (UNRESOLVED == state) {
            moduleDto.setUnresolvedComponents(module.unresolvedComponents());
            moduleDto.setResolvedComponents(module.resolvedComponents());
        }
        if (ERROR == state) {
            Collection<ErrorDto> errors = errorsMapper.map(module.errors());
            moduleDto.setErrors(errors);
        }
        return moduleDto;
    }
}
