package de.codecentric.reedelk.runtime.api.annotation;

import java.lang.annotation.*;

/**
 * This annotation determines whether a property is visible and serializable.
 * A property might only be visible and serializable when another property
 * has a given value, for example: HTTP Security configuration should be visible
 * and serializable only when the chosen HTTP protocol is 'HTTPS'.
 */
@Repeatable(Whens.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface When {

    String NULL = "###NULL###";
    String BLANK = "###BLANK###";
    String NOT_BLANK = "###NOT_BLANK###";
    String SCRIPT = "###SCRIPT###";
    String NOT_SCRIPT = "###NOT_SCRIPT###";

    /**
     * The related property name on which we want to verify if
     * a given value is currently assigned to it.
     */
    String propertyName();

    /**
     * The target value we want the condition to be true for.
     */
    String propertyValue();
}
