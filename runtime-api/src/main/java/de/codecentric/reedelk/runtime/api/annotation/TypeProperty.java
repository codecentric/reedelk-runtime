package de.codecentric.reedelk.runtime.api.annotation;

import de.codecentric.reedelk.runtime.api.commons.StringUtils;

import java.lang.annotation.*;

@Repeatable(TypeProperties.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface TypeProperty {

    String USE_DEFAULT_NAME = "###USE_DEFAULT_NAME###";

    String name() default USE_DEFAULT_NAME;

    String example() default StringUtils.EMPTY;

    String description() default StringUtils.EMPTY;

    Class<?> type() default UseDefaultType.class;

}
