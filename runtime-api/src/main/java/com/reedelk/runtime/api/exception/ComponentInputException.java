package com.reedelk.runtime.api.exception;

import com.reedelk.runtime.api.component.Implementor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class ComponentInputException extends PlatformException {

    public ComponentInputException(Class<? extends Implementor> implementor, Object actualInputType, Class<?> ...expectedInputTypes) {
        super(formatErrorMessage(implementor, actualInputType, expectedInputTypes));
    }

    private static String formatErrorMessage(Class<? extends Implementor> implementor, Object actualInputType, Class<?> ...expectedInputTypes) {
        String actualType = topmostInterfaceOf(actualInputType);
        List<String> expectedTypes = Arrays.stream(expectedInputTypes).map(Class::getSimpleName).collect(toList());
        if (expectedTypes.size() > 1) {
            return format("%s (%s) was invoked with a not supported Input Type: actual=[%s], expected one of=[%s].",
                    implementor.getSimpleName(),
                    implementor.getName(),
                    actualType,
                    String.join(",", expectedTypes));
        } else {
            return format("%s (%s) was invoked with a not supported Input Type: actual=[%s], expected=[%s].",
                    implementor.getSimpleName(),
                    implementor.getName(),
                    actualType,
                    String.join(",", expectedTypes));
        }
    }

    private static String topmostInterfaceOf(Object actualInputType) {
        if (actualInputType == null) return null;
        if (List.class.isAssignableFrom(actualInputType.getClass())) {
            return List.class.getSimpleName();
        } else if (Map.class.isAssignableFrom(actualInputType.getClass())) {
            return Map.class.getSimpleName();
        } else {
            return actualInputType.getClass().getSimpleName();
        }
    }
}
