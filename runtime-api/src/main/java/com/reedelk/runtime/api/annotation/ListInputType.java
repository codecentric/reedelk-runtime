package com.reedelk.runtime.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to control the input property type
 * as a selector for the list.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ListInputType {

    ListInput value() default ListInput.DEFAULT;

    enum ListInput {
        FILE,
        DEFAULT
    }
}
