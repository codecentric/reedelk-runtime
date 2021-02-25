package de.codecentric.reedelk.runtime.converter.types.doubletype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsFloat implements ValueConverter<Double,Float> {

    @Override
    public Float from(Double value) {
        return value.floatValue();
    }
}
