package com.reedelk.runtime.converter.types.stringtype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsObject implements ValueConverter<String,Object> {

    @Override
    public Object from(String value) {
        return value;
    }
}
