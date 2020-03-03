package com.reedelk.runtime.converter.types.booleantype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsObject implements ValueConverter<Boolean, Object> {

    @Override
    public Object from(Boolean value) {
        return value;
    }
}
