package com.reedelk.runtime.converter.types.floattype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsString implements ValueConverter<Float,String> {

    @Override
    public String from(Float value) {
        return String.valueOf(value);
    }
}
