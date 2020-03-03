package com.reedelk.runtime.converter.types.doubletype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsObject implements ValueConverter<Double,Object> {

    @Override
    public Object from(Double value) {
        return value;
    }
}
