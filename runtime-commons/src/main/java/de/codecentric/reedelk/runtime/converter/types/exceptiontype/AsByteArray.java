package de.codecentric.reedelk.runtime.converter.types.exceptiontype;

import de.codecentric.reedelk.runtime.api.commons.StackTraceUtils;
import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsByteArray implements ValueConverter<Exception,byte[]> {

    @Override
    public byte[] from(Exception value) {
        return StackTraceUtils.asByteArray(value);
    }
}
