package de.codecentric.reedelk.runtime.api.annotation;

import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ComponentOutput {

    // Message attributes are *ALWAYS* in a map. See Message Attributes interface.
    Class<? extends MessageAttributes>[] attributes() default MessageAttributes.class;

    Class<?>[] payload() default Object.class;

    String description() default StringUtils.EMPTY;

    // The name of the dynamic expression property from which
    // the payload type should be inferred from.
    String dynamicPropertyName() default StringUtils.EMPTY;

    // Marker interface to let the auto-completion know that
    // the payload and attributes should be taken from the previous component instead.
    // We extend this class from message attributes in order to use this class
    // inside the attributes array as well.
    class PreviousComponent extends MessageAttributes {
    }

    class InferFromDynamicProperty {
    }
}
