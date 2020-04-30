package com.reedelk.runtime.api.annotation;

import com.reedelk.runtime.api.commons.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Type {

    // Identifies a global type containing static functions accessible from all script contexts
    // such as the Util, Log, Config types.
    boolean global() default false;

    // Used only for lists, to know which type they hold.
    Class<?> listItemType() default UseDefaultType.class;

    String description() default StringUtils.EMPTY;

    // For Objects extending Map -> Map (can be overridden for special cases where Map is extended with custom methods and becomes an API base type)
    // For Objects extending List -> List
    // Object class simple name in all the other cases.
    String USE_DEFAULT_DISPLAY_NAME = "###USE_DEFAULT_DISPLAY_NAME###";

    String displayName() default USE_DEFAULT_DISPLAY_NAME;
}
