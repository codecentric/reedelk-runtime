package com.reedelk.runtime.api.annotation;

import java.lang.annotation.*;

@Repeatable(AutocompleteVariables.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AutocompleteVariable {

    String name();

    Class<?> type();
}
