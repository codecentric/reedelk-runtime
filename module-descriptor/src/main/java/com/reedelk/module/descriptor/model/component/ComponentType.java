package com.reedelk.module.descriptor.model.component;

public enum ComponentType {

    INBOUND,
    PROCESSOR,
    JOIN,
    // The component type is used for non-standard processors/inbound/join components such as Fork/Try-Catch and so on.
    COMPONENT,
    UNKNOWN

}
