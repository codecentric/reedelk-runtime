package de.codecentric.reedelk.runtime.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Combo {
    /**
     * If true, a custom value can be added to the combo, false otherwise.
     */
    boolean editable() default false;

    /**
     * Combo values to be displayed in the combo drop down.
     */
    String[] comboValues() default {};

    /**
     * The display prototype to be used to determine the width of the dropdown combo.
     */
    String prototype() default "XXXXXX";
}
