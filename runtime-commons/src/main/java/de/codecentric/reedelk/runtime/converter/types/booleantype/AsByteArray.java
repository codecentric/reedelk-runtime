package de.codecentric.reedelk.runtime.converter.types.booleantype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsByteArray implements ValueConverter<Boolean,byte[]> {

    @Override
    public byte[] from(Boolean value) {
        byte byteVal = (byte) (value == Boolean.TRUE ? 1 : 0);
        return new byte[]{byteVal};
    }
}
