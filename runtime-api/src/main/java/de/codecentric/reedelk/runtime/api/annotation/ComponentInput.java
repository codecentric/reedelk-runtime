package de.codecentric.reedelk.runtime.api.annotation;

import de.codecentric.reedelk.runtime.api.commons.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ComponentInput {

    Class<?>[] payload() default Object.class;

    String description() default StringUtils.EMPTY;

}
