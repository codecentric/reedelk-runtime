package com.reedelk.runtime.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {
    /**
     * Special value that indicates that the default display name should be used
     * for the module this annotation is referring to.
     */
    String USE_DEFAULT_NAME = "###USE_DEFAULT_NAME###";

    String value() default USE_DEFAULT_NAME;

    boolean builtIn() default false;

}
