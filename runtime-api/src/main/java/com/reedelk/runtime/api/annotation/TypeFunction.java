package com.reedelk.runtime.api.annotation;

import com.reedelk.runtime.api.commons.StringUtils;

import java.lang.annotation.*;

@Repeatable(TypeFunctions.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TypeFunction {

    String USE_DEFAULT_NAME = "###USE_DEFAULT_NAME###";

    String name() default USE_DEFAULT_NAME;

    String USE_DEFAULT_SIGNATURE = "###USE_DEFAULT_SIGNATURE###";

    String signature() default USE_DEFAULT_SIGNATURE;

    String example() default StringUtils.EMPTY;

    String description() default StringUtils.EMPTY;

    Class<?> returnType() default UseDefaultType.class;

    /**
     * An offset to place the cursor right where the user can start edit
     * the arguments, for example myMap.put('|'): the cursor with offset 2 will be placed
     * between the ' '.
     */
    int cursorOffset() default 0;
}
