package com.reedelk.runtime.converter.types.exceptiontype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsObject implements ValueConverter<Exception,Object> {

    @Override
    public Object from(Exception value) {
        return value;
    }
}
