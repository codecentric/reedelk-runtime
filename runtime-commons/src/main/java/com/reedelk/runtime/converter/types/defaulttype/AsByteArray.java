package com.reedelk.runtime.converter.types.defaulttype;

import com.reedelk.runtime.commons.ObjectToBytes;
import com.reedelk.runtime.converter.types.ValueConverter;

class AsByteArray implements ValueConverter<Object,byte[]> {

    @Override
    public byte[] from(Object value) {
        return ObjectToBytes.from(value);
    }
}
