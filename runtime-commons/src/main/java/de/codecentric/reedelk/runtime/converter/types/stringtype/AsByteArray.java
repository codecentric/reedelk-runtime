package de.codecentric.reedelk.runtime.converter.types.stringtype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsByteArray implements ValueConverter<String,byte[]> {

    @Override
    public byte[] from(String value) {
        return value.getBytes();
    }
}
