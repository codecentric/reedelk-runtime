package com.reedelk.runtime.converter.types.booleantype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsFloat implements ValueConverter<Boolean,Float> {

    @Override
    public Float from(Boolean value) {
        return value == Boolean.TRUE ? 1f : 0f;
    }
}
