package de.codecentric.reedelk.runtime.converter.types.bytearraytype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsObject implements ValueConverter<byte[],Object> {

    @Override
    public Object from(byte[] value) {
        return value;
    }
}
