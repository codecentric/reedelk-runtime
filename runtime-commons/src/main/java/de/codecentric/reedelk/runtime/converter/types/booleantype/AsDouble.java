package de.codecentric.reedelk.runtime.converter.types.booleantype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsDouble implements ValueConverter<Boolean, Double> {

    @Override
    public Double from(Boolean value) {
        return value == Boolean.TRUE ? 1d : 0d;
    }
}
