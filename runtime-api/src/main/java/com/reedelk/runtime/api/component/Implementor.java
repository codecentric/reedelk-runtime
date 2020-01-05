package com.reedelk.runtime.api.component;

public interface Implementor {

    /**
     * This method is called right after the Implementor object is being
     * instantiated by the runtime, when a flow is being built. Override
     * it to perform initial resources allocation before the component
     * is being used.
     */
    default void initialize() {
    }

    /**
     * This method is called on each Implementor when a flow is being stopped
     * and removed from the runtime. Override it to cleanup previously initialized
     * resources before removing it from the runtime.
     */
    default void dispose() {
    }
}
