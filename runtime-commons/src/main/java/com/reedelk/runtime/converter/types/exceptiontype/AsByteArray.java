package com.reedelk.runtime.converter.types.exceptiontype;

import com.reedelk.runtime.api.commons.StackTraceUtils;
import com.reedelk.runtime.converter.types.ValueConverter;

class AsByteArray implements ValueConverter<Exception,byte[]> {

    @Override
    public byte[] from(Exception value) {
        return StackTraceUtils.asByteArray(value);
    }
}
