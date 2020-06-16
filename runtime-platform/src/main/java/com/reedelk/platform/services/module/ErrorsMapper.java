package com.reedelk.platform.services.module;

import com.reedelk.runtime.api.commons.StackTraceUtils;
import com.reedelk.runtime.system.api.ErrorDto;

import java.util.Collection;
import java.util.stream.Collectors;

public class ErrorsMapper {

    public Collection<ErrorDto> map(Collection<Exception> exceptions) {
        return exceptions.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private ErrorDto map(Exception exception) {
        ErrorDto dto = new ErrorDto();
        dto.setStacktrace(StackTraceUtils.asString(exception));
        dto.setMessage(exception.getMessage());
        return dto;
    }
}
