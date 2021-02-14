package de.codecentric.reedelk.runtime.converter.types.doubletype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsBoolean implements ValueConverter<Double,Boolean> {

    @Override
    public Boolean from(Double value) {
        return value == 1d;
    }
}
