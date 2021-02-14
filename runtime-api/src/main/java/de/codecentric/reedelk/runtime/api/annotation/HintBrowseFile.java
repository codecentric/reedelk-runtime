package de.codecentric.reedelk.runtime.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface HintBrowseFile {
    // The value of the 'hint' text  to be used
    // in the browse input field of the property this
    // annotation is referring to.
    String value();
}
