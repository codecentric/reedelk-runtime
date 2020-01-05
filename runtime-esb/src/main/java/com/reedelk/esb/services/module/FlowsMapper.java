package com.reedelk.esb.services.module;

import com.reedelk.esb.flow.Flow;
import com.reedelk.runtime.system.api.FlowDto;

import java.util.Collection;
import java.util.stream.Collectors;

public class FlowsMapper {

    public Collection<FlowDto> map(Collection<Flow> flows) {
        return flows.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private FlowDto map(Flow flow) {
        FlowDto dto = new FlowDto();
        dto.setId(flow.getFlowId());
        flow.getFlowTitle().ifPresent(dto::setTitle);
        return dto;
    }
}
