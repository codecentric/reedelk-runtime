package com.reedelk.runtime.converter.types.defaulttype;

import com.reedelk.runtime.commons.ObjectToBytes;
import com.reedelk.runtime.converter.types.ValueConverter;

import java.io.Serializable;

class AsByteArray implements ValueConverter<Object,byte[]> {

    @Override
    public byte[] from(Object value) {
        if (value instanceof Serializable) {
            return ObjectToBytes.from(value);
        } else {
            String string = value.toString();
            return string.getBytes();
        }
    }
}