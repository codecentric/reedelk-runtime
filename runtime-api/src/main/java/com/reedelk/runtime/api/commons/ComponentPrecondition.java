package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.exception.ComponentInputException;

public class ComponentPrecondition {

    public static class Input {

        private Input() {
        }

        /**
         * Checks if the input object is one of the types given in the list.
         * An exception is thrown in the input object does not have one of the
         * expected types or if the input is object is null.
         */
        public static void requireTypeMatches(Class<? extends Implementor> implementor, Object input, Class<?> ...expected) {
            if (input == null) throw new ComponentInputException(implementor, input, expected);
            for (Class<?> current : expected) {
                if (current.isAssignableFrom(input.getClass())) {
                    return;
                }
            }
            throw new ComponentInputException(implementor, input, expected);
        }

        /**
         * Checks if the input object is one of the types given in the list.
         * An exception is thrown in the input object does not have one of the expected types.
         */
        public static void requireTypeMatchesOrNull(Class<? extends Implementor> implementor, Object input, Class<?> ...expected) {
            if (input == null) return; // It is ok if null.
            requireTypeMatches(implementor, input, expected);
        }
    }
}
