package com.reedelk.runtime.converter.types.stringtype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsBoolean implements ValueConverter<String,Boolean> {

    @Override
    public Boolean from(String value) {
        return Boolean.parseBoolean(value);
    }
}
