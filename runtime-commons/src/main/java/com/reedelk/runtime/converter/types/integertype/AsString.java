package com.reedelk.runtime.converter.types.integertype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsString implements ValueConverter<Integer,String> {

    @Override
    public String from(Integer value) {
        return String.valueOf(value);
    }
}
