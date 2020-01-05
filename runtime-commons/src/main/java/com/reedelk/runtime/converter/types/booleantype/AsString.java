package com.reedelk.runtime.converter.types.booleantype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsString implements ValueConverter<Boolean,String> {

    @Override
    public String from(Boolean value) {
        return String.valueOf(value);
    }
}
