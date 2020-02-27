package com.reedelk.runtime.api.annotation;

import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;

import java.lang.annotation.*;

@Repeatable(AutocompleteItems.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AutocompleteItem {

    interface UseDefaultReturnType {
    }

    String USE_DEFAULT_TOKEN = "###USE_DEFAULT_TOKEN###";

    String token() default USE_DEFAULT_TOKEN;

    Class<?> returnType() default UseDefaultReturnType.class;

    String description() default "";

    String replaceValue();

    int cursorOffset() default 0;

    /*
     * An autocomplete item could be a function or a variable.
     */
    AutocompleteItemType itemType() default AutocompleteItemType.FUNCTION;

}
