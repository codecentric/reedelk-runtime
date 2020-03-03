package com.reedelk.runtime.converter.types.floattype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsObject implements ValueConverter<Float,Object> {

    @Override
    public Object from(Float value) {
        return value;
    }
}
