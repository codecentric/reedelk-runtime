package com.reedelk.runtime.api.annotation;

import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.message.MessageAttributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ComponentOutput {

    // Message attributes are *ALWAYS* in a map. See Message Attributes interface.
    Class<? extends MessageAttributes> attributes() default MessageAttributes.class;

    Class<?>[] payload() default Object.class;

    String description() default StringUtils.EMPTY;
}
