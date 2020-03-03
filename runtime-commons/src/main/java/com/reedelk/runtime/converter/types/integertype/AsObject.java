package com.reedelk.runtime.converter.types.integertype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsObject implements ValueConverter<Integer,Object> {

    @Override
    public Object from(Integer value) {
        return value;
    }
}
