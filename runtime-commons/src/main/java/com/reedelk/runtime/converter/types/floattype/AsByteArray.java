package com.reedelk.runtime.converter.types.floattype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsByteArray implements ValueConverter<Float,byte[]> {

    @Override
    public byte[] from(Float value) {
        return new byte[] {value.byteValue()};
    }
}
