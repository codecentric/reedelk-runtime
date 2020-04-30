package com.reedelk.runtime.api.annotation;

import java.lang.annotation.*;

@Repeatable(ScriptVariables.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ScriptVariable {

    String name();

    Class<?> type();
}
