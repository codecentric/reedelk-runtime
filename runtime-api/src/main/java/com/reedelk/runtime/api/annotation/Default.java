package com.reedelk.runtime.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Default {
    /**
     * Special value that indicates that the default value should be used
     * for the property this annotation is referring to.
     */
    String USE_DEFAULT_VALUE = "###USE_DEFAULT_VALUE###";


    String value() default USE_DEFAULT_VALUE;
}
