package com.reedelk.runtime.converter.types.floattype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsInteger implements ValueConverter<Float,Integer> {

    @Override
    public Integer from(Float value) {
        return value.intValue();
    }
}
