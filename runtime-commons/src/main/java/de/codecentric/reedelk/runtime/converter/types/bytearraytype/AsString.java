package de.codecentric.reedelk.runtime.converter.types.bytearraytype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsString implements ValueConverter<byte[],String> {

    @Override
    public String from(byte[] value) {
        return new String(value);
    }
}
