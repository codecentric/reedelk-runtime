package com.reedelk.runtime.converter.types.integertype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsDouble implements ValueConverter<Integer,Double> {

    @Override
    public Double from(Integer value) {
        return value.doubleValue();
    }
}
