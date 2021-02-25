package de.codecentric.reedelk.runtime.converter.types.floattype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsDouble implements ValueConverter<Float,Double> {

    @Override
    public Double from(Float value) {
        return value.doubleValue();
    }
}
