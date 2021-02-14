package de.codecentric.reedelk.runtime.converter.types.booleantype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsObject implements ValueConverter<Boolean, Object> {

    @Override
    public Object from(Boolean value) {
        return value;
    }
}
