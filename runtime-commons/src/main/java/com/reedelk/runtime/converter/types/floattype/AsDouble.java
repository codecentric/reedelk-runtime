package com.reedelk.runtime.converter.types.floattype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsDouble implements ValueConverter<Float,Double> {

    @Override
    public Double from(Float value) {
        return value.doubleValue();
    }
}
