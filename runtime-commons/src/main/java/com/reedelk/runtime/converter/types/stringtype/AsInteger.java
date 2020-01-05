package com.reedelk.runtime.converter.types.stringtype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsInteger implements ValueConverter<String,Integer> {

    @Override
    public Integer from(String value) {
        return Integer.parseInt(value);
    }
}
