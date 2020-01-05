package com.reedelk.runtime.converter.types.integertype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsByteArray implements ValueConverter<Integer,byte[]> {

    @Override
    public byte[] from(Integer value) {
        return new byte[] {value.byteValue()};
    }
}
