package de.codecentric.reedelk.runtime.converter.types.booleantype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsInteger implements ValueConverter<Boolean, Integer> {

    @Override
    public Integer from(Boolean value) {
        return value == Boolean.TRUE ? 1 : 0;
    }
}
