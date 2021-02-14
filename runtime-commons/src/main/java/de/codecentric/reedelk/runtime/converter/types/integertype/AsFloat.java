package de.codecentric.reedelk.runtime.converter.types.integertype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsFloat implements ValueConverter<Integer,Float> {

    @Override
    public Float from(Integer value) {
        return value.floatValue();
    }
}
