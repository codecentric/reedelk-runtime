package com.reedelk.runtime.converter.types.doubletype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsInteger implements ValueConverter<Double,Integer> {

    @Override
    public Integer from(Double value) {
        return  value.intValue();
    }
}
