package com.reedelk.runtime.converter.types.integertype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsBoolean implements ValueConverter<Integer,Boolean> {

    @Override
    public Boolean from(Integer value) {
        return value == 1;
    }
}
