package de.codecentric.reedelk.platform.services.module;

import de.codecentric.reedelk.runtime.api.commons.StackTraceUtils;
import de.codecentric.reedelk.runtime.system.api.ErrorDto;

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
