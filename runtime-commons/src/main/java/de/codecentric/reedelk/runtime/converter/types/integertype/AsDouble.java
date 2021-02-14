package de.codecentric.reedelk.runtime.converter.types.integertype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsDouble implements ValueConverter<Integer,Double> {

    @Override
    public Double from(Integer value) {
        return value.doubleValue();
    }
}
