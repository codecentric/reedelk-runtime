package com.reedelk.runtime.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleComponent {
    /**
     * Special value that indicates that the default name should be used
     * for the component this annotation is referring to.
     */
    String USE_DEFAULT_NAME = "###USE_DEFAULT_NAME###";

    String name() default USE_DEFAULT_NAME;

    /**
     * Special value that indicates that the default description should be used
     * for the component this annotation is referring to.
     */
    String USE_DEFAULT_DESCRIPTION = "###USE_DEFAULT_DESCRIPTION###";

    String description() default USE_DEFAULT_DESCRIPTION;

}
