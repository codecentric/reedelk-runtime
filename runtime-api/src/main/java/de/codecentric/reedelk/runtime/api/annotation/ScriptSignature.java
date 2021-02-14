package de.codecentric.reedelk.runtime.api.annotation;

import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ScriptSignature {

    String[] arguments() default {"context","message"};

    Class<?>[] types() default {FlowContext.class, Message.class};
}
