package com.reedelk.runtime.converter.types.stringtype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsLong implements ValueConverter<String,Long> {

    @Override
    public Long from(String value) {
        return Long.parseLong(value);
    }
}
