package com.reedelk.module.descriptor.model;

import java.io.Serializable;

public interface TypeDescriptor extends Serializable {

    Class<?> getType();

    default void setType(Class<?> type) {}

}
