package de.codecentric.reedelk.module.descriptor.model.property;

import java.io.Serializable;

public interface PropertyTypeDescriptor extends Serializable {

    Class<?> getType();

    default void setType(Class<?> type) {}

}
